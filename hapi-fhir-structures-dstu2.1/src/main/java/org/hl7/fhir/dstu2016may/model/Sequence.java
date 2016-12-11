package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
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
    public static class SequenceReferenceSeqComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.
         */
        @Child(name = "chromosome", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The chromosome containing the genetic finding", formalDefinition="The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used." )
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
         * 0-based start position (inclusive) of the window on the reference sequence.
         */
        @Child(name = "windowStart", type = {IntegerType.class}, order=6, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based start position (inclusive) of the window on the  reference sequence", formalDefinition="0-based start position (inclusive) of the window on the reference sequence." )
        protected IntegerType windowStart;

        /**
         * 0-based end position (exclusive) of the window on the reference sequence.
         */
        @Child(name = "windowEnd", type = {IntegerType.class}, order=7, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based end position (exclusive) of the window on the reference sequence", formalDefinition="0-based end position (exclusive) of the window on the reference sequence." )
        protected IntegerType windowEnd;

        private static final long serialVersionUID = -165922935L;

    /**
     * Constructor
     */
      public SequenceReferenceSeqComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SequenceReferenceSeqComponent(CodeableConcept referenceSeqId, IntegerType windowStart, IntegerType windowEnd) {
        super();
        this.referenceSeqId = referenceSeqId;
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
      }

        /**
         * @return {@link #chromosome} (The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.)
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
         * @param value {@link #chromosome} (The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.)
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
         * @return {@link #windowStart} (0-based start position (inclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowStart" gives direct access to the value
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
         * @param value {@link #windowStart} (0-based start position (inclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowStart" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setWindowStartElement(IntegerType value) { 
          this.windowStart = value;
          return this;
        }

        /**
         * @return 0-based start position (inclusive) of the window on the reference sequence.
         */
        public int getWindowStart() { 
          return this.windowStart == null || this.windowStart.isEmpty() ? 0 : this.windowStart.getValue();
        }

        /**
         * @param value 0-based start position (inclusive) of the window on the reference sequence.
         */
        public SequenceReferenceSeqComponent setWindowStart(int value) { 
            if (this.windowStart == null)
              this.windowStart = new IntegerType();
            this.windowStart.setValue(value);
          return this;
        }

        /**
         * @return {@link #windowEnd} (0-based end position (exclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
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
         * @param value {@link #windowEnd} (0-based end position (exclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setWindowEndElement(IntegerType value) { 
          this.windowEnd = value;
          return this;
        }

        /**
         * @return 0-based end position (exclusive) of the window on the reference sequence.
         */
        public int getWindowEnd() { 
          return this.windowEnd == null || this.windowEnd.isEmpty() ? 0 : this.windowEnd.getValue();
        }

        /**
         * @param value 0-based end position (exclusive) of the window on the reference sequence.
         */
        public SequenceReferenceSeqComponent setWindowEnd(int value) { 
            if (this.windowEnd == null)
              this.windowEnd = new IntegerType();
            this.windowEnd.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("chromosome", "CodeableConcept", "The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.", 0, java.lang.Integer.MAX_VALUE, chromosome));
          childrenList.add(new Property("genomeBuild", "string", "The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.", 0, java.lang.Integer.MAX_VALUE, genomeBuild));
          childrenList.add(new Property("referenceSeqId", "CodeableConcept", "Reference identifier of reference sequence submitted to NCBI. It must match the type in the Sequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.", 0, java.lang.Integer.MAX_VALUE, referenceSeqId));
          childrenList.add(new Property("referenceSeqPointer", "Reference(Sequence)", "A Pointer to another Sequence entity as refence sequence.", 0, java.lang.Integer.MAX_VALUE, referenceSeqPointer));
          childrenList.add(new Property("referenceSeqString", "string", "A Reference Sequence string.", 0, java.lang.Integer.MAX_VALUE, referenceSeqString));
          childrenList.add(new Property("windowStart", "integer", "0-based start position (inclusive) of the window on the reference sequence.", 0, java.lang.Integer.MAX_VALUE, windowStart));
          childrenList.add(new Property("windowEnd", "integer", "0-based end position (exclusive) of the window on the reference sequence.", 0, java.lang.Integer.MAX_VALUE, windowEnd));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1499470472: /*chromosome*/ return this.chromosome == null ? new Base[0] : new Base[] {this.chromosome}; // CodeableConcept
        case 1061239735: /*genomeBuild*/ return this.genomeBuild == null ? new Base[0] : new Base[] {this.genomeBuild}; // StringType
        case -1911500465: /*referenceSeqId*/ return this.referenceSeqId == null ? new Base[0] : new Base[] {this.referenceSeqId}; // CodeableConcept
        case 1923414665: /*referenceSeqPointer*/ return this.referenceSeqPointer == null ? new Base[0] : new Base[] {this.referenceSeqPointer}; // Reference
        case -1648301499: /*referenceSeqString*/ return this.referenceSeqString == null ? new Base[0] : new Base[] {this.referenceSeqString}; // StringType
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
           && compareDeep(referenceSeqString, o.referenceSeqString, true) && compareDeep(windowStart, o.windowStart, true)
           && compareDeep(windowEnd, o.windowEnd, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceReferenceSeqComponent))
          return false;
        SequenceReferenceSeqComponent o = (SequenceReferenceSeqComponent) other;
        return compareValues(genomeBuild, o.genomeBuild, true) && compareValues(referenceSeqString, o.referenceSeqString, true)
           && compareValues(windowStart, o.windowStart, true) && compareValues(windowEnd, o.windowEnd, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (chromosome == null || chromosome.isEmpty()) && (genomeBuild == null || genomeBuild.isEmpty())
           && (referenceSeqId == null || referenceSeqId.isEmpty()) && (referenceSeqPointer == null || referenceSeqPointer.isEmpty())
           && (referenceSeqString == null || referenceSeqString.isEmpty()) && (windowStart == null || windowStart.isEmpty())
           && (windowEnd == null || windowEnd.isEmpty());
      }

  public String fhirType() {
    return "Sequence.referenceSeq";

  }

  }

    @Block()
    public static class SequenceVariationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * 0-based start position (inclusive) of the variation on the  reference sequence.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based start position (inclusive) of the variation on the  reference sequence", formalDefinition="0-based start position (inclusive) of the variation on the  reference sequence." )
        protected IntegerType start;

        /**
         * 0-based end position (exclusive) of the variation on the reference sequence.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based end position (exclusive) of the variation on the reference sequence", formalDefinition="0-based end position (exclusive) of the variation on the reference sequence." )
        protected IntegerType end;

        /**
         * Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
         */
        @Child(name = "observedAllele", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Nucleotide(s)/amino acids from start position to stop position of observed variation", formalDefinition="Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand." )
        protected StringType observedAllele;

        /**
         * Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
         */
        @Child(name = "referenceAllele", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Nucleotide(s)/amino acids from start position to stop position of reference variation", formalDefinition="Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand." )
        protected StringType referenceAllele;

        /**
         * Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).
         */
        @Child(name = "cigar", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Extended CIGAR string for aligning the sequence with reference bases", formalDefinition="Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm)." )
        protected StringType cigar;

        private static final long serialVersionUID = 913298829L;

    /**
     * Constructor
     */
      public SequenceVariationComponent() {
        super();
      }

        /**
         * @return {@link #start} (0-based start position (inclusive) of the variation on the  reference sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceVariationComponent.start");
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
         * @param value {@link #start} (0-based start position (inclusive) of the variation on the  reference sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceVariationComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return 0-based start position (inclusive) of the variation on the  reference sequence.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value 0-based start position (inclusive) of the variation on the  reference sequence.
         */
        public SequenceVariationComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (0-based end position (exclusive) of the variation on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceVariationComponent.end");
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
         * @param value {@link #end} (0-based end position (exclusive) of the variation on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceVariationComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return 0-based end position (exclusive) of the variation on the reference sequence.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value 0-based end position (exclusive) of the variation on the reference sequence.
         */
        public SequenceVariationComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        /**
         * @return {@link #observedAllele} (Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getObservedAllele" gives direct access to the value
         */
        public StringType getObservedAlleleElement() { 
          if (this.observedAllele == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceVariationComponent.observedAllele");
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
        public SequenceVariationComponent setObservedAlleleElement(StringType value) { 
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
        public SequenceVariationComponent setObservedAllele(String value) { 
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
              throw new Error("Attempt to auto-create SequenceVariationComponent.referenceAllele");
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
        public SequenceVariationComponent setReferenceAlleleElement(StringType value) { 
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
        public SequenceVariationComponent setReferenceAllele(String value) { 
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
              throw new Error("Attempt to auto-create SequenceVariationComponent.cigar");
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
        public SequenceVariationComponent setCigarElement(StringType value) { 
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
        public SequenceVariationComponent setCigar(String value) { 
          if (Utilities.noString(value))
            this.cigar = null;
          else {
            if (this.cigar == null)
              this.cigar = new StringType();
            this.cigar.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("start", "integer", "0-based start position (inclusive) of the variation on the  reference sequence.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "0-based end position (exclusive) of the variation on the reference sequence.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("observedAllele", "string", "Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.", 0, java.lang.Integer.MAX_VALUE, observedAllele));
          childrenList.add(new Property("referenceAllele", "string", "Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.", 0, java.lang.Integer.MAX_VALUE, referenceAllele));
          childrenList.add(new Property("cigar", "string", "Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).", 0, java.lang.Integer.MAX_VALUE, cigar));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // IntegerType
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // IntegerType
        case -1418745787: /*observedAllele*/ return this.observedAllele == null ? new Base[0] : new Base[] {this.observedAllele}; // StringType
        case 364045960: /*referenceAllele*/ return this.referenceAllele == null ? new Base[0] : new Base[] {this.referenceAllele}; // StringType
        case 94658738: /*cigar*/ return this.cigar == null ? new Base[0] : new Base[] {this.cigar}; // StringType
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
        else
          return super.addChild(name);
      }

      public SequenceVariationComponent copy() {
        SequenceVariationComponent dst = new SequenceVariationComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.observedAllele = observedAllele == null ? null : observedAllele.copy();
        dst.referenceAllele = referenceAllele == null ? null : referenceAllele.copy();
        dst.cigar = cigar == null ? null : cigar.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceVariationComponent))
          return false;
        SequenceVariationComponent o = (SequenceVariationComponent) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true) && compareDeep(observedAllele, o.observedAllele, true)
           && compareDeep(referenceAllele, o.referenceAllele, true) && compareDeep(cigar, o.cigar, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceVariationComponent))
          return false;
        SequenceVariationComponent o = (SequenceVariationComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(observedAllele, o.observedAllele, true)
           && compareValues(referenceAllele, o.referenceAllele, true) && compareValues(cigar, o.cigar, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
           && (observedAllele == null || observedAllele.isEmpty()) && (referenceAllele == null || referenceAllele.isEmpty())
           && (cigar == null || cigar.isEmpty());
      }

  public String fhirType() {
    return "Sequence.variation";

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
         * Method for quality.
         */
        @Child(name = "method", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Method for quality", formalDefinition="Method for quality." )
        protected StringType method;

        private static final long serialVersionUID = -1046665930L;

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
         * @return {@link #method} (Method for quality.). This is the underlying object with id, value and extensions. The accessor "getMethod" gives direct access to the value
         */
        public StringType getMethodElement() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceQualityComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new StringType(); // bb
          return this.method;
        }

        public boolean hasMethodElement() { 
          return this.method != null && !this.method.isEmpty();
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (Method for quality.). This is the underlying object with id, value and extensions. The accessor "getMethod" gives direct access to the value
         */
        public SequenceQualityComponent setMethodElement(StringType value) { 
          this.method = value;
          return this;
        }

        /**
         * @return Method for quality.
         */
        public String getMethod() { 
          return this.method == null ? null : this.method.getValue();
        }

        /**
         * @param value Method for quality.
         */
        public SequenceQualityComponent setMethod(String value) { 
          if (Utilities.noString(value))
            this.method = null;
          else {
            if (this.method == null)
              this.method = new StringType();
            this.method.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("start", "integer", "0-based start position (inclusive) of the sequence.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "0-based end position (exclusive) of the sequence.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("score", "Quantity", "Quality score.", 0, java.lang.Integer.MAX_VALUE, score));
          childrenList.add(new Property("method", "string", "Method for quality.", 0, java.lang.Integer.MAX_VALUE, method));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // IntegerType
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // IntegerType
        case 109264530: /*score*/ return this.score == null ? new Base[0] : new Base[] {this.score}; // Quantity
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // StringType
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
        case 109264530: // score
          this.score = castToQuantity(value); // Quantity
          break;
        case -1077554975: // method
          this.method = castToString(value); // StringType
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
        else if (name.equals("score"))
          this.score = castToQuantity(value); // Quantity
        else if (name.equals("method"))
          this.method = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: throw new FHIRException("Cannot make property start as it is not a complex type"); // IntegerType
        case 100571: throw new FHIRException("Cannot make property end as it is not a complex type"); // IntegerType
        case 109264530:  return getScore(); // Quantity
        case -1077554975: throw new FHIRException("Cannot make property method as it is not a complex type"); // StringType
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
        else if (name.equals("score")) {
          this.score = new Quantity();
          return this.score;
        }
        else if (name.equals("method")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.method");
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
        dst.method = method == null ? null : method.copy();
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
           && compareDeep(method, o.method, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceQualityComponent))
          return false;
        SequenceQualityComponent o = (SequenceQualityComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(method, o.method, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
           && (score == null || score.isEmpty()) && (method == null || method.isEmpty());
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
         * Id of the variation in this external repository.
         */
        @Child(name = "variantId", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of the variant", formalDefinition="Id of the variation in this external repository." )
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
         * @return {@link #variantId} (Id of the variation in this external repository.). This is the underlying object with id, value and extensions. The accessor "getVariantId" gives direct access to the value
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
         * @param value {@link #variantId} (Id of the variation in this external repository.). This is the underlying object with id, value and extensions. The accessor "getVariantId" gives direct access to the value
         */
        public SequenceRepositoryComponent setVariantIdElement(StringType value) { 
          this.variantId = value;
          return this;
        }

        /**
         * @return Id of the variation in this external repository.
         */
        public String getVariantId() { 
          return this.variantId == null ? null : this.variantId.getValue();
        }

        /**
         * @param value Id of the variation in this external repository.
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
          childrenList.add(new Property("variantId", "string", "Id of the variation in this external repository.", 0, java.lang.Integer.MAX_VALUE, variantId));
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
        return super.isEmpty() && (url == null || url.isEmpty()) && (name == null || name.isEmpty())
           && (variantId == null || variantId.isEmpty()) && (readId == null || readId.isEmpty());
      }

  public String fhirType() {
    return "Sequence.repository";

  }

  }

    @Block()
    public static class SequenceStructureVariationComponent extends BackboneElement implements IBaseBackboneElement {
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
        protected SequenceStructureVariationOuterComponent outer;

        /**
         * Structural variant inner.
         */
        @Child(name = "inner", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="", formalDefinition="Structural variant inner." )
        protected SequenceStructureVariationInnerComponent inner;

        private static final long serialVersionUID = -1615654736L;

    /**
     * Constructor
     */
      public SequenceStructureVariationComponent() {
        super();
      }

        /**
         * @return {@link #precisionOfBoundaries} (Precision of boundaries.). This is the underlying object with id, value and extensions. The accessor "getPrecisionOfBoundaries" gives direct access to the value
         */
        public StringType getPrecisionOfBoundariesElement() { 
          if (this.precisionOfBoundaries == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariationComponent.precisionOfBoundaries");
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
        public SequenceStructureVariationComponent setPrecisionOfBoundariesElement(StringType value) { 
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
        public SequenceStructureVariationComponent setPrecisionOfBoundaries(String value) { 
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
              throw new Error("Attempt to auto-create SequenceStructureVariationComponent.reportedaCGHRatio");
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
        public SequenceStructureVariationComponent setReportedaCGHRatioElement(DecimalType value) { 
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
        public SequenceStructureVariationComponent setReportedaCGHRatio(BigDecimal value) { 
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
        public SequenceStructureVariationComponent setReportedaCGHRatio(long value) { 
              this.reportedaCGHRatio = new DecimalType();
            this.reportedaCGHRatio.setValue(value);
          return this;
        }

        /**
         * @param value Structural Variant reported aCGH ratio.
         */
        public SequenceStructureVariationComponent setReportedaCGHRatio(double value) { 
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
              throw new Error("Attempt to auto-create SequenceStructureVariationComponent.length");
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
        public SequenceStructureVariationComponent setLengthElement(IntegerType value) { 
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
        public SequenceStructureVariationComponent setLength(int value) { 
            if (this.length == null)
              this.length = new IntegerType();
            this.length.setValue(value);
          return this;
        }

        /**
         * @return {@link #outer} (Structural variant outer.)
         */
        public SequenceStructureVariationOuterComponent getOuter() { 
          if (this.outer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariationComponent.outer");
            else if (Configuration.doAutoCreate())
              this.outer = new SequenceStructureVariationOuterComponent(); // cc
          return this.outer;
        }

        public boolean hasOuter() { 
          return this.outer != null && !this.outer.isEmpty();
        }

        /**
         * @param value {@link #outer} (Structural variant outer.)
         */
        public SequenceStructureVariationComponent setOuter(SequenceStructureVariationOuterComponent value) { 
          this.outer = value;
          return this;
        }

        /**
         * @return {@link #inner} (Structural variant inner.)
         */
        public SequenceStructureVariationInnerComponent getInner() { 
          if (this.inner == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariationComponent.inner");
            else if (Configuration.doAutoCreate())
              this.inner = new SequenceStructureVariationInnerComponent(); // cc
          return this.inner;
        }

        public boolean hasInner() { 
          return this.inner != null && !this.inner.isEmpty();
        }

        /**
         * @param value {@link #inner} (Structural variant inner.)
         */
        public SequenceStructureVariationComponent setInner(SequenceStructureVariationInnerComponent value) { 
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
        case 106111099: /*outer*/ return this.outer == null ? new Base[0] : new Base[] {this.outer}; // SequenceStructureVariationOuterComponent
        case 100355670: /*inner*/ return this.inner == null ? new Base[0] : new Base[] {this.inner}; // SequenceStructureVariationInnerComponent
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
          this.outer = (SequenceStructureVariationOuterComponent) value; // SequenceStructureVariationOuterComponent
          break;
        case 100355670: // inner
          this.inner = (SequenceStructureVariationInnerComponent) value; // SequenceStructureVariationInnerComponent
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
          this.outer = (SequenceStructureVariationOuterComponent) value; // SequenceStructureVariationOuterComponent
        else if (name.equals("inner"))
          this.inner = (SequenceStructureVariationInnerComponent) value; // SequenceStructureVariationInnerComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1591532317: throw new FHIRException("Cannot make property precisionOfBoundaries as it is not a complex type"); // StringType
        case -1872600587: throw new FHIRException("Cannot make property reportedaCGHRatio as it is not a complex type"); // DecimalType
        case -1106363674: throw new FHIRException("Cannot make property length as it is not a complex type"); // IntegerType
        case 106111099:  return getOuter(); // SequenceStructureVariationOuterComponent
        case 100355670:  return getInner(); // SequenceStructureVariationInnerComponent
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
          this.outer = new SequenceStructureVariationOuterComponent();
          return this.outer;
        }
        else if (name.equals("inner")) {
          this.inner = new SequenceStructureVariationInnerComponent();
          return this.inner;
        }
        else
          return super.addChild(name);
      }

      public SequenceStructureVariationComponent copy() {
        SequenceStructureVariationComponent dst = new SequenceStructureVariationComponent();
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
        if (!(other instanceof SequenceStructureVariationComponent))
          return false;
        SequenceStructureVariationComponent o = (SequenceStructureVariationComponent) other;
        return compareDeep(precisionOfBoundaries, o.precisionOfBoundaries, true) && compareDeep(reportedaCGHRatio, o.reportedaCGHRatio, true)
           && compareDeep(length, o.length, true) && compareDeep(outer, o.outer, true) && compareDeep(inner, o.inner, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceStructureVariationComponent))
          return false;
        SequenceStructureVariationComponent o = (SequenceStructureVariationComponent) other;
        return compareValues(precisionOfBoundaries, o.precisionOfBoundaries, true) && compareValues(reportedaCGHRatio, o.reportedaCGHRatio, true)
           && compareValues(length, o.length, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (precisionOfBoundaries == null || precisionOfBoundaries.isEmpty())
           && (reportedaCGHRatio == null || reportedaCGHRatio.isEmpty()) && (length == null || length.isEmpty())
           && (outer == null || outer.isEmpty()) && (inner == null || inner.isEmpty());
      }

  public String fhirType() {
    return "Sequence.structureVariation";

  }

  }

    @Block()
    public static class SequenceStructureVariationOuterComponent extends BackboneElement implements IBaseBackboneElement {
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
      public SequenceStructureVariationOuterComponent() {
        super();
      }

        /**
         * @return {@link #start} (Structural Variant Outer Start-End.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariationOuterComponent.start");
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
        public SequenceStructureVariationOuterComponent setStartElement(IntegerType value) { 
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
        public SequenceStructureVariationOuterComponent setStart(int value) { 
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
              throw new Error("Attempt to auto-create SequenceStructureVariationOuterComponent.end");
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
        public SequenceStructureVariationOuterComponent setEndElement(IntegerType value) { 
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
        public SequenceStructureVariationOuterComponent setEnd(int value) { 
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

      public SequenceStructureVariationOuterComponent copy() {
        SequenceStructureVariationOuterComponent dst = new SequenceStructureVariationOuterComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceStructureVariationOuterComponent))
          return false;
        SequenceStructureVariationOuterComponent o = (SequenceStructureVariationOuterComponent) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceStructureVariationOuterComponent))
          return false;
        SequenceStructureVariationOuterComponent o = (SequenceStructureVariationOuterComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
          ;
      }

  public String fhirType() {
    return "Sequence.structureVariation.outer";

  }

  }

    @Block()
    public static class SequenceStructureVariationInnerComponent extends BackboneElement implements IBaseBackboneElement {
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
      public SequenceStructureVariationInnerComponent() {
        super();
      }

        /**
         * @return {@link #start} (Structural Variant Inner Start-End.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariationInnerComponent.start");
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
        public SequenceStructureVariationInnerComponent setStartElement(IntegerType value) { 
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
        public SequenceStructureVariationInnerComponent setStart(int value) { 
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
              throw new Error("Attempt to auto-create SequenceStructureVariationInnerComponent.end");
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
        public SequenceStructureVariationInnerComponent setEndElement(IntegerType value) { 
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
        public SequenceStructureVariationInnerComponent setEnd(int value) { 
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

      public SequenceStructureVariationInnerComponent copy() {
        SequenceStructureVariationInnerComponent dst = new SequenceStructureVariationInnerComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceStructureVariationInnerComponent))
          return false;
        SequenceStructureVariationInnerComponent o = (SequenceStructureVariationInnerComponent) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceStructureVariationInnerComponent))
          return false;
        SequenceStructureVariationInnerComponent o = (SequenceStructureVariationInnerComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
          ;
      }

  public String fhirType() {
    return "Sequence.structureVariation.inner";

  }

  }

    /**
     * Amino acid / cDNA transcript / RNA variation.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="AA | DNA | RNA", formalDefinition="Amino acid / cDNA transcript / RNA variation." )
    protected Enumeration<SequenceType> type;

    /**
     * The patient, or group of patients whose sequencing results are described by this resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what this is about", formalDefinition="The patient, or group of patients whose sequencing results are described by this resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient, or group of patients whose sequencing results are described by this resource.)
     */
    protected Patient patientTarget;

    /**
     * Specimen used for sequencing.
     */
    @Child(name = "specimen", type = {Specimen.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Specimen used for sequencing", formalDefinition="Specimen used for sequencing." )
    protected Reference specimen;

    /**
     * The actual object that is the target of the reference (Specimen used for sequencing.)
     */
    protected Specimen specimenTarget;

    /**
     * The method for sequencing, for example, chip information.
     */
    @Child(name = "device", type = {Device.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The method for sequencing", formalDefinition="The method for sequencing, for example, chip information." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The method for sequencing, for example, chip information.)
     */
    protected Device deviceTarget;

    /**
     * Quantity of the sequence.
     */
    @Child(name = "quantity", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Quantity of the sequence", formalDefinition="Quantity of the sequence." )
    protected Quantity quantity;

    /**
     * The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria.
     */
    @Child(name = "species", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Supporting tests of human, viruses, and bacteria", formalDefinition="The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria." )
    protected CodeableConcept species;

    /**
     * Reference Sequence. It can be described in two ways. One is provide the unique identifier of reference sequence submitted to NCBI. The start and end position of window on reference sequence should be defined.  The other way is using  genome build, chromosome number,and also the start, end position of window (this method is specifically for DNA reference sequence) .
     */
    @Child(name = "referenceSeq", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reference sequence", formalDefinition="Reference Sequence. It can be described in two ways. One is provide the unique identifier of reference sequence submitted to NCBI. The start and end position of window on reference sequence should be defined.  The other way is using  genome build, chromosome number,and also the start, end position of window (this method is specifically for DNA reference sequence) ." )
    protected List<SequenceReferenceSeqComponent> referenceSeq;

    /**
     * Variation info in this sequence.
     */
    @Child(name = "variation", type = {}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Variation info in this sequence", formalDefinition="Variation info in this sequence." )
    protected SequenceVariationComponent variation;

    /**
     * Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing.
     */
    @Child(name = "quality", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Sequence Quality", formalDefinition="Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing." )
    protected List<SequenceQualityComponent> quality;

    /**
     * The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA.
     */
    @Child(name = "allelicState", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The level of occurrence of a single DNA Sequence Variation within a set of chromosomes: Heteroplasmic / Homoplasmic / Homozygous / Heterozygous / Hemizygous", formalDefinition="The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA." )
    protected CodeableConcept allelicState;

    /**
     * Allele frequencies.
     */
    @Child(name = "allelicFrequency", type = {DecimalType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Allele frequencies", formalDefinition="Allele frequencies." )
    protected DecimalType allelicFrequency;

    /**
     * Values: amplificaiton / deletion / LOH.
     */
    @Child(name = "copyNumberEvent", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Copy Number Event: Values: amplificaiton / deletion / LOH", formalDefinition="Values: amplificaiton / deletion / LOH." )
    protected CodeableConcept copyNumberEvent;

    /**
     * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.
     */
    @Child(name = "readCoverage", type = {IntegerType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Average number of reads representing a given nucleotide in the reconstructed sequence", formalDefinition="Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence." )
    protected IntegerType readCoverage;

    /**
     * Configurations of the external repository.
     */
    @Child(name = "repository", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External repository", formalDefinition="Configurations of the external repository." )
    protected List<SequenceRepositoryComponent> repository;

    /**
     * Pointer to next atomic sequence which at most contains one variation.
     */
    @Child(name = "pointer", type = {Sequence.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Pointer to next atomic sequence", formalDefinition="Pointer to next atomic sequence which at most contains one variation." )
    protected List<Reference> pointer;
    /**
     * The actual objects that are the target of the reference (Pointer to next atomic sequence which at most contains one variation.)
     */
    protected List<Sequence> pointerTarget;


    /**
     * Observed Sequence.
     */
    @Child(name = "observedSeq", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Observed Sequence", formalDefinition="Observed Sequence." )
    protected StringType observedSeq;

    /**
     * Analysis of the sequence.
     */
    @Child(name = "observation", type = {Observation.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Observation-genetics", formalDefinition="Analysis of the sequence." )
    protected Reference observation;

    /**
     * The actual object that is the target of the reference (Analysis of the sequence.)
     */
    protected Observation observationTarget;

    /**
     * Structural variant.
     */
    @Child(name = "structureVariation", type = {}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="Structural variant." )
    protected SequenceStructureVariationComponent structureVariation;

    private static final long serialVersionUID = -1153660995L;

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
     * @return {@link #patient} (The patient, or group of patients whose sequencing results are described by this resource.)
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
     * @param value {@link #patient} (The patient, or group of patients whose sequencing results are described by this resource.)
     */
    public Sequence setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient, or group of patients whose sequencing results are described by this resource.)
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
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient, or group of patients whose sequencing results are described by this resource.)
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
     * @return {@link #referenceSeq} (Reference Sequence. It can be described in two ways. One is provide the unique identifier of reference sequence submitted to NCBI. The start and end position of window on reference sequence should be defined.  The other way is using  genome build, chromosome number,and also the start, end position of window (this method is specifically for DNA reference sequence) .)
     */
    public List<SequenceReferenceSeqComponent> getReferenceSeq() { 
      if (this.referenceSeq == null)
        this.referenceSeq = new ArrayList<SequenceReferenceSeqComponent>();
      return this.referenceSeq;
    }

    public boolean hasReferenceSeq() { 
      if (this.referenceSeq == null)
        return false;
      for (SequenceReferenceSeqComponent item : this.referenceSeq)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #referenceSeq} (Reference Sequence. It can be described in two ways. One is provide the unique identifier of reference sequence submitted to NCBI. The start and end position of window on reference sequence should be defined.  The other way is using  genome build, chromosome number,and also the start, end position of window (this method is specifically for DNA reference sequence) .)
     */
    // syntactic sugar
    public SequenceReferenceSeqComponent addReferenceSeq() { //3
      SequenceReferenceSeqComponent t = new SequenceReferenceSeqComponent();
      if (this.referenceSeq == null)
        this.referenceSeq = new ArrayList<SequenceReferenceSeqComponent>();
      this.referenceSeq.add(t);
      return t;
    }

    // syntactic sugar
    public Sequence addReferenceSeq(SequenceReferenceSeqComponent t) { //3
      if (t == null)
        return this;
      if (this.referenceSeq == null)
        this.referenceSeq = new ArrayList<SequenceReferenceSeqComponent>();
      this.referenceSeq.add(t);
      return this;
    }

    /**
     * @return {@link #variation} (Variation info in this sequence.)
     */
    public SequenceVariationComponent getVariation() { 
      if (this.variation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.variation");
        else if (Configuration.doAutoCreate())
          this.variation = new SequenceVariationComponent(); // cc
      return this.variation;
    }

    public boolean hasVariation() { 
      return this.variation != null && !this.variation.isEmpty();
    }

    /**
     * @param value {@link #variation} (Variation info in this sequence.)
     */
    public Sequence setVariation(SequenceVariationComponent value) { 
      this.variation = value;
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
     * @return {@link #repository} (Configurations of the external repository.)
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
     * @return {@link #repository} (Configurations of the external repository.)
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

    /**
     * @return {@link #pointer} (Pointer to next atomic sequence which at most contains one variation.)
     */
    public List<Reference> getPointer() { 
      if (this.pointer == null)
        this.pointer = new ArrayList<Reference>();
      return this.pointer;
    }

    public boolean hasPointer() { 
      if (this.pointer == null)
        return false;
      for (Reference item : this.pointer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #pointer} (Pointer to next atomic sequence which at most contains one variation.)
     */
    // syntactic sugar
    public Reference addPointer() { //3
      Reference t = new Reference();
      if (this.pointer == null)
        this.pointer = new ArrayList<Reference>();
      this.pointer.add(t);
      return t;
    }

    // syntactic sugar
    public Sequence addPointer(Reference t) { //3
      if (t == null)
        return this;
      if (this.pointer == null)
        this.pointer = new ArrayList<Reference>();
      this.pointer.add(t);
      return this;
    }

    /**
     * @return {@link #pointer} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Pointer to next atomic sequence which at most contains one variation.)
     */
    public List<Sequence> getPointerTarget() { 
      if (this.pointerTarget == null)
        this.pointerTarget = new ArrayList<Sequence>();
      return this.pointerTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #pointer} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Pointer to next atomic sequence which at most contains one variation.)
     */
    public Sequence addPointerTarget() { 
      Sequence r = new Sequence();
      if (this.pointerTarget == null)
        this.pointerTarget = new ArrayList<Sequence>();
      this.pointerTarget.add(r);
      return r;
    }

    /**
     * @return {@link #observedSeq} (Observed Sequence.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
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
     * @param value {@link #observedSeq} (Observed Sequence.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
     */
    public Sequence setObservedSeqElement(StringType value) { 
      this.observedSeq = value;
      return this;
    }

    /**
     * @return Observed Sequence.
     */
    public String getObservedSeq() { 
      return this.observedSeq == null ? null : this.observedSeq.getValue();
    }

    /**
     * @param value Observed Sequence.
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
     * @return {@link #observation} (Analysis of the sequence.)
     */
    public Reference getObservation() { 
      if (this.observation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.observation");
        else if (Configuration.doAutoCreate())
          this.observation = new Reference(); // cc
      return this.observation;
    }

    public boolean hasObservation() { 
      return this.observation != null && !this.observation.isEmpty();
    }

    /**
     * @param value {@link #observation} (Analysis of the sequence.)
     */
    public Sequence setObservation(Reference value) { 
      this.observation = value;
      return this;
    }

    /**
     * @return {@link #observation} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Analysis of the sequence.)
     */
    public Observation getObservationTarget() { 
      if (this.observationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.observation");
        else if (Configuration.doAutoCreate())
          this.observationTarget = new Observation(); // aa
      return this.observationTarget;
    }

    /**
     * @param value {@link #observation} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Analysis of the sequence.)
     */
    public Sequence setObservationTarget(Observation value) { 
      this.observationTarget = value;
      return this;
    }

    /**
     * @return {@link #structureVariation} (Structural variant.)
     */
    public SequenceStructureVariationComponent getStructureVariation() { 
      if (this.structureVariation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.structureVariation");
        else if (Configuration.doAutoCreate())
          this.structureVariation = new SequenceStructureVariationComponent(); // cc
      return this.structureVariation;
    }

    public boolean hasStructureVariation() { 
      return this.structureVariation != null && !this.structureVariation.isEmpty();
    }

    /**
     * @param value {@link #structureVariation} (Structural variant.)
     */
    public Sequence setStructureVariation(SequenceStructureVariationComponent value) { 
      this.structureVariation = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "Amino acid / cDNA transcript / RNA variation.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient, or group of patients whose sequencing results are described by this resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "Specimen used for sequencing.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("device", "Reference(Device)", "The method for sequencing, for example, chip information.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("quantity", "Quantity", "Quantity of the sequence.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("species", "CodeableConcept", "The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria.", 0, java.lang.Integer.MAX_VALUE, species));
        childrenList.add(new Property("referenceSeq", "", "Reference Sequence. It can be described in two ways. One is provide the unique identifier of reference sequence submitted to NCBI. The start and end position of window on reference sequence should be defined.  The other way is using  genome build, chromosome number,and also the start, end position of window (this method is specifically for DNA reference sequence) .", 0, java.lang.Integer.MAX_VALUE, referenceSeq));
        childrenList.add(new Property("variation", "", "Variation info in this sequence.", 0, java.lang.Integer.MAX_VALUE, variation));
        childrenList.add(new Property("quality", "", "Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing.", 0, java.lang.Integer.MAX_VALUE, quality));
        childrenList.add(new Property("allelicState", "CodeableConcept", "The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA.", 0, java.lang.Integer.MAX_VALUE, allelicState));
        childrenList.add(new Property("allelicFrequency", "decimal", "Allele frequencies.", 0, java.lang.Integer.MAX_VALUE, allelicFrequency));
        childrenList.add(new Property("copyNumberEvent", "CodeableConcept", "Values: amplificaiton / deletion / LOH.", 0, java.lang.Integer.MAX_VALUE, copyNumberEvent));
        childrenList.add(new Property("readCoverage", "integer", "Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.", 0, java.lang.Integer.MAX_VALUE, readCoverage));
        childrenList.add(new Property("repository", "", "Configurations of the external repository.", 0, java.lang.Integer.MAX_VALUE, repository));
        childrenList.add(new Property("pointer", "Reference(Sequence)", "Pointer to next atomic sequence which at most contains one variation.", 0, java.lang.Integer.MAX_VALUE, pointer));
        childrenList.add(new Property("observedSeq", "string", "Observed Sequence.", 0, java.lang.Integer.MAX_VALUE, observedSeq));
        childrenList.add(new Property("observation", "Reference(Observation)", "Analysis of the sequence.", 0, java.lang.Integer.MAX_VALUE, observation));
        childrenList.add(new Property("structureVariation", "", "Structural variant.", 0, java.lang.Integer.MAX_VALUE, structureVariation));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<SequenceType>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -2132868344: /*specimen*/ return this.specimen == null ? new Base[0] : new Base[] {this.specimen}; // Reference
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : new Base[] {this.device}; // Reference
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -2008465092: /*species*/ return this.species == null ? new Base[0] : new Base[] {this.species}; // CodeableConcept
        case -502547180: /*referenceSeq*/ return this.referenceSeq == null ? new Base[0] : this.referenceSeq.toArray(new Base[this.referenceSeq.size()]); // SequenceReferenceSeqComponent
        case -81944045: /*variation*/ return this.variation == null ? new Base[0] : new Base[] {this.variation}; // SequenceVariationComponent
        case 651215103: /*quality*/ return this.quality == null ? new Base[0] : this.quality.toArray(new Base[this.quality.size()]); // SequenceQualityComponent
        case 2079026319: /*allelicState*/ return this.allelicState == null ? new Base[0] : new Base[] {this.allelicState}; // CodeableConcept
        case 8650330: /*allelicFrequency*/ return this.allelicFrequency == null ? new Base[0] : new Base[] {this.allelicFrequency}; // DecimalType
        case 960854556: /*copyNumberEvent*/ return this.copyNumberEvent == null ? new Base[0] : new Base[] {this.copyNumberEvent}; // CodeableConcept
        case -1798816354: /*readCoverage*/ return this.readCoverage == null ? new Base[0] : new Base[] {this.readCoverage}; // IntegerType
        case 1950800714: /*repository*/ return this.repository == null ? new Base[0] : this.repository.toArray(new Base[this.repository.size()]); // SequenceRepositoryComponent
        case -400605635: /*pointer*/ return this.pointer == null ? new Base[0] : this.pointer.toArray(new Base[this.pointer.size()]); // Reference
        case 125541495: /*observedSeq*/ return this.observedSeq == null ? new Base[0] : new Base[] {this.observedSeq}; // StringType
        case 122345516: /*observation*/ return this.observation == null ? new Base[0] : new Base[] {this.observation}; // Reference
        case 1886586336: /*structureVariation*/ return this.structureVariation == null ? new Base[0] : new Base[] {this.structureVariation}; // SequenceStructureVariationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new SequenceTypeEnumFactory().fromType(value); // Enumeration<SequenceType>
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
        case -2008465092: // species
          this.species = castToCodeableConcept(value); // CodeableConcept
          break;
        case -502547180: // referenceSeq
          this.getReferenceSeq().add((SequenceReferenceSeqComponent) value); // SequenceReferenceSeqComponent
          break;
        case -81944045: // variation
          this.variation = (SequenceVariationComponent) value; // SequenceVariationComponent
          break;
        case 651215103: // quality
          this.getQuality().add((SequenceQualityComponent) value); // SequenceQualityComponent
          break;
        case 2079026319: // allelicState
          this.allelicState = castToCodeableConcept(value); // CodeableConcept
          break;
        case 8650330: // allelicFrequency
          this.allelicFrequency = castToDecimal(value); // DecimalType
          break;
        case 960854556: // copyNumberEvent
          this.copyNumberEvent = castToCodeableConcept(value); // CodeableConcept
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
        case 125541495: // observedSeq
          this.observedSeq = castToString(value); // StringType
          break;
        case 122345516: // observation
          this.observation = castToReference(value); // Reference
          break;
        case 1886586336: // structureVariation
          this.structureVariation = (SequenceStructureVariationComponent) value; // SequenceStructureVariationComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new SequenceTypeEnumFactory().fromType(value); // Enumeration<SequenceType>
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("specimen"))
          this.specimen = castToReference(value); // Reference
        else if (name.equals("device"))
          this.device = castToReference(value); // Reference
        else if (name.equals("quantity"))
          this.quantity = castToQuantity(value); // Quantity
        else if (name.equals("species"))
          this.species = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("referenceSeq"))
          this.getReferenceSeq().add((SequenceReferenceSeqComponent) value);
        else if (name.equals("variation"))
          this.variation = (SequenceVariationComponent) value; // SequenceVariationComponent
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
        else if (name.equals("repository"))
          this.getRepository().add((SequenceRepositoryComponent) value);
        else if (name.equals("pointer"))
          this.getPointer().add(castToReference(value));
        else if (name.equals("observedSeq"))
          this.observedSeq = castToString(value); // StringType
        else if (name.equals("observation"))
          this.observation = castToReference(value); // Reference
        else if (name.equals("structureVariation"))
          this.structureVariation = (SequenceStructureVariationComponent) value; // SequenceStructureVariationComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<SequenceType>
        case -791418107:  return getPatient(); // Reference
        case -2132868344:  return getSpecimen(); // Reference
        case -1335157162:  return getDevice(); // Reference
        case -1285004149:  return getQuantity(); // Quantity
        case -2008465092:  return getSpecies(); // CodeableConcept
        case -502547180:  return addReferenceSeq(); // SequenceReferenceSeqComponent
        case -81944045:  return getVariation(); // SequenceVariationComponent
        case 651215103:  return addQuality(); // SequenceQualityComponent
        case 2079026319:  return getAllelicState(); // CodeableConcept
        case 8650330: throw new FHIRException("Cannot make property allelicFrequency as it is not a complex type"); // DecimalType
        case 960854556:  return getCopyNumberEvent(); // CodeableConcept
        case -1798816354: throw new FHIRException("Cannot make property readCoverage as it is not a complex type"); // IntegerType
        case 1950800714:  return addRepository(); // SequenceRepositoryComponent
        case -400605635:  return addPointer(); // Reference
        case 125541495: throw new FHIRException("Cannot make property observedSeq as it is not a complex type"); // StringType
        case 122345516:  return getObservation(); // Reference
        case 1886586336:  return getStructureVariation(); // SequenceStructureVariationComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.type");
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
        else if (name.equals("species")) {
          this.species = new CodeableConcept();
          return this.species;
        }
        else if (name.equals("referenceSeq")) {
          return addReferenceSeq();
        }
        else if (name.equals("variation")) {
          this.variation = new SequenceVariationComponent();
          return this.variation;
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
        else if (name.equals("repository")) {
          return addRepository();
        }
        else if (name.equals("pointer")) {
          return addPointer();
        }
        else if (name.equals("observedSeq")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.observedSeq");
        }
        else if (name.equals("observation")) {
          this.observation = new Reference();
          return this.observation;
        }
        else if (name.equals("structureVariation")) {
          this.structureVariation = new SequenceStructureVariationComponent();
          return this.structureVariation;
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
        dst.patient = patient == null ? null : patient.copy();
        dst.specimen = specimen == null ? null : specimen.copy();
        dst.device = device == null ? null : device.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.species = species == null ? null : species.copy();
        if (referenceSeq != null) {
          dst.referenceSeq = new ArrayList<SequenceReferenceSeqComponent>();
          for (SequenceReferenceSeqComponent i : referenceSeq)
            dst.referenceSeq.add(i.copy());
        };
        dst.variation = variation == null ? null : variation.copy();
        if (quality != null) {
          dst.quality = new ArrayList<SequenceQualityComponent>();
          for (SequenceQualityComponent i : quality)
            dst.quality.add(i.copy());
        };
        dst.allelicState = allelicState == null ? null : allelicState.copy();
        dst.allelicFrequency = allelicFrequency == null ? null : allelicFrequency.copy();
        dst.copyNumberEvent = copyNumberEvent == null ? null : copyNumberEvent.copy();
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
        dst.observedSeq = observedSeq == null ? null : observedSeq.copy();
        dst.observation = observation == null ? null : observation.copy();
        dst.structureVariation = structureVariation == null ? null : structureVariation.copy();
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
        return compareDeep(type, o.type, true) && compareDeep(patient, o.patient, true) && compareDeep(specimen, o.specimen, true)
           && compareDeep(device, o.device, true) && compareDeep(quantity, o.quantity, true) && compareDeep(species, o.species, true)
           && compareDeep(referenceSeq, o.referenceSeq, true) && compareDeep(variation, o.variation, true)
           && compareDeep(quality, o.quality, true) && compareDeep(allelicState, o.allelicState, true) && compareDeep(allelicFrequency, o.allelicFrequency, true)
           && compareDeep(copyNumberEvent, o.copyNumberEvent, true) && compareDeep(readCoverage, o.readCoverage, true)
           && compareDeep(repository, o.repository, true) && compareDeep(pointer, o.pointer, true) && compareDeep(observedSeq, o.observedSeq, true)
           && compareDeep(observation, o.observation, true) && compareDeep(structureVariation, o.structureVariation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Sequence))
          return false;
        Sequence o = (Sequence) other;
        return compareValues(type, o.type, true) && compareValues(allelicFrequency, o.allelicFrequency, true)
           && compareValues(readCoverage, o.readCoverage, true) && compareValues(observedSeq, o.observedSeq, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (patient == null || patient.isEmpty())
           && (specimen == null || specimen.isEmpty()) && (device == null || device.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (species == null || species.isEmpty()) && (referenceSeq == null || referenceSeq.isEmpty())
           && (variation == null || variation.isEmpty()) && (quality == null || quality.isEmpty()) && (allelicState == null || allelicState.isEmpty())
           && (allelicFrequency == null || allelicFrequency.isEmpty()) && (copyNumberEvent == null || copyNumberEvent.isEmpty())
           && (readCoverage == null || readCoverage.isEmpty()) && (repository == null || repository.isEmpty())
           && (pointer == null || pointer.isEmpty()) && (observedSeq == null || observedSeq.isEmpty())
           && (observation == null || observation.isEmpty()) && (structureVariation == null || structureVariation.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Sequence;
   }

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The subject that the observation is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Sequence.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Sequence.patient", description="The subject that the observation is about", type="reference" )
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
   * Search parameter: <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variation.start</b><br>
   * </p>
   */
  @SearchParamDefinition(name="start", path="Sequence.variation.start", description="Start position (0-based inclusive) of the sequence", type="number" )
  public static final String SP_START = "start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variation.start</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam START = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_START);

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
   * Search parameter: <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variation.end</b><br>
   * </p>
   */
  @SearchParamDefinition(name="end", path="Sequence.variation.end", description="End position (0-based exclusive) of the sequence", type="number" )
  public static final String SP_END = "end";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variation.end</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam END = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_END);


}

