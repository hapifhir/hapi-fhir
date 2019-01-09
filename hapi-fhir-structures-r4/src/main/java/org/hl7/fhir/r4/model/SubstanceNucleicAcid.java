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
 * Nucleic acids are defined by three distinct elements: the base, sugar and linkage. Individual substance/moiety IDs will be created for each of these elements. The nucleotide sequence will be always entered in the 5’-3’ direction.
 */
@ResourceDef(name="SubstanceNucleicAcid", profile="http://hl7.org/fhir/StructureDefinition/SubstanceNucleicAcid")
public class SubstanceNucleicAcid extends DomainResource {

    @Block()
    public static class SubstanceNucleicAcidSubunitComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         */
        @Child(name = "subunit", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts", formalDefinition="Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts." )
        protected IntegerType subunit;

        /**
         * Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.
         */
        @Child(name = "sequence", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured", formalDefinition="Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured." )
        protected StringType sequence;

        /**
         * The length of the sequence shall be captured.
         */
        @Child(name = "length", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The length of the sequence shall be captured", formalDefinition="The length of the sequence shall be captured." )
        protected IntegerType length;

        /**
         * (TBC).
         */
        @Child(name = "sequenceAttachment", type = {Attachment.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="(TBC)", formalDefinition="(TBC)." )
        protected Attachment sequenceAttachment;

        /**
         * The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A separate representation would be redundant.
         */
        @Child(name = "fivePrime", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A separate representation would be redundant", formalDefinition="The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A separate representation would be redundant." )
        protected CodeableConcept fivePrime;

        /**
         * The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A separate representation would be redundant.
         */
        @Child(name = "threePrime", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A separate representation would be redundant", formalDefinition="The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A separate representation would be redundant." )
        protected CodeableConcept threePrime;

        /**
         * The linkages between sugar residues will also be captured.
         */
        @Child(name = "linkage", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The linkages between sugar residues will also be captured", formalDefinition="The linkages between sugar residues will also be captured." )
        protected List<SubstanceNucleicAcidSubunitLinkageComponent> linkage;

        /**
         * 5.3.6.8.1 Sugar ID (Mandatory).
         */
        @Child(name = "sugar", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="5.3.6.8.1 Sugar ID (Mandatory)", formalDefinition="5.3.6.8.1 Sugar ID (Mandatory)." )
        protected List<SubstanceNucleicAcidSubunitSugarComponent> sugar;

        private static final long serialVersionUID = 1835593659L;

    /**
     * Constructor
     */
      public SubstanceNucleicAcidSubunitComponent() {
        super();
      }

        /**
         * @return {@link #subunit} (Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.). This is the underlying object with id, value and extensions. The accessor "getSubunit" gives direct access to the value
         */
        public IntegerType getSubunitElement() { 
          if (this.subunit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitComponent.subunit");
            else if (Configuration.doAutoCreate())
              this.subunit = new IntegerType(); // bb
          return this.subunit;
        }

        public boolean hasSubunitElement() { 
          return this.subunit != null && !this.subunit.isEmpty();
        }

        public boolean hasSubunit() { 
          return this.subunit != null && !this.subunit.isEmpty();
        }

        /**
         * @param value {@link #subunit} (Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.). This is the underlying object with id, value and extensions. The accessor "getSubunit" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitComponent setSubunitElement(IntegerType value) { 
          this.subunit = value;
          return this;
        }

        /**
         * @return Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         */
        public int getSubunit() { 
          return this.subunit == null || this.subunit.isEmpty() ? 0 : this.subunit.getValue();
        }

        /**
         * @param value Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         */
        public SubstanceNucleicAcidSubunitComponent setSubunit(int value) { 
            if (this.subunit == null)
              this.subunit = new IntegerType();
            this.subunit.setValue(value);
          return this;
        }

        /**
         * @return {@link #sequence} (Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public StringType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new StringType(); // bb
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitComponent setSequenceElement(StringType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.
         */
        public String getSequence() { 
          return this.sequence == null ? null : this.sequence.getValue();
        }

        /**
         * @param value Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.
         */
        public SubstanceNucleicAcidSubunitComponent setSequence(String value) { 
          if (Utilities.noString(value))
            this.sequence = null;
          else {
            if (this.sequence == null)
              this.sequence = new StringType();
            this.sequence.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #length} (The length of the sequence shall be captured.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public IntegerType getLengthElement() { 
          if (this.length == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitComponent.length");
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
         * @param value {@link #length} (The length of the sequence shall be captured.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitComponent setLengthElement(IntegerType value) { 
          this.length = value;
          return this;
        }

        /**
         * @return The length of the sequence shall be captured.
         */
        public int getLength() { 
          return this.length == null || this.length.isEmpty() ? 0 : this.length.getValue();
        }

        /**
         * @param value The length of the sequence shall be captured.
         */
        public SubstanceNucleicAcidSubunitComponent setLength(int value) { 
            if (this.length == null)
              this.length = new IntegerType();
            this.length.setValue(value);
          return this;
        }

        /**
         * @return {@link #sequenceAttachment} ((TBC).)
         */
        public Attachment getSequenceAttachment() { 
          if (this.sequenceAttachment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitComponent.sequenceAttachment");
            else if (Configuration.doAutoCreate())
              this.sequenceAttachment = new Attachment(); // cc
          return this.sequenceAttachment;
        }

        public boolean hasSequenceAttachment() { 
          return this.sequenceAttachment != null && !this.sequenceAttachment.isEmpty();
        }

        /**
         * @param value {@link #sequenceAttachment} ((TBC).)
         */
        public SubstanceNucleicAcidSubunitComponent setSequenceAttachment(Attachment value) { 
          this.sequenceAttachment = value;
          return this;
        }

        /**
         * @return {@link #fivePrime} (The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A separate representation would be redundant.)
         */
        public CodeableConcept getFivePrime() { 
          if (this.fivePrime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitComponent.fivePrime");
            else if (Configuration.doAutoCreate())
              this.fivePrime = new CodeableConcept(); // cc
          return this.fivePrime;
        }

        public boolean hasFivePrime() { 
          return this.fivePrime != null && !this.fivePrime.isEmpty();
        }

        /**
         * @param value {@link #fivePrime} (The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A separate representation would be redundant.)
         */
        public SubstanceNucleicAcidSubunitComponent setFivePrime(CodeableConcept value) { 
          this.fivePrime = value;
          return this;
        }

        /**
         * @return {@link #threePrime} (The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A separate representation would be redundant.)
         */
        public CodeableConcept getThreePrime() { 
          if (this.threePrime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitComponent.threePrime");
            else if (Configuration.doAutoCreate())
              this.threePrime = new CodeableConcept(); // cc
          return this.threePrime;
        }

        public boolean hasThreePrime() { 
          return this.threePrime != null && !this.threePrime.isEmpty();
        }

        /**
         * @param value {@link #threePrime} (The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A separate representation would be redundant.)
         */
        public SubstanceNucleicAcidSubunitComponent setThreePrime(CodeableConcept value) { 
          this.threePrime = value;
          return this;
        }

        /**
         * @return {@link #linkage} (The linkages between sugar residues will also be captured.)
         */
        public List<SubstanceNucleicAcidSubunitLinkageComponent> getLinkage() { 
          if (this.linkage == null)
            this.linkage = new ArrayList<SubstanceNucleicAcidSubunitLinkageComponent>();
          return this.linkage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceNucleicAcidSubunitComponent setLinkage(List<SubstanceNucleicAcidSubunitLinkageComponent> theLinkage) { 
          this.linkage = theLinkage;
          return this;
        }

        public boolean hasLinkage() { 
          if (this.linkage == null)
            return false;
          for (SubstanceNucleicAcidSubunitLinkageComponent item : this.linkage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceNucleicAcidSubunitLinkageComponent addLinkage() { //3
          SubstanceNucleicAcidSubunitLinkageComponent t = new SubstanceNucleicAcidSubunitLinkageComponent();
          if (this.linkage == null)
            this.linkage = new ArrayList<SubstanceNucleicAcidSubunitLinkageComponent>();
          this.linkage.add(t);
          return t;
        }

        public SubstanceNucleicAcidSubunitComponent addLinkage(SubstanceNucleicAcidSubunitLinkageComponent t) { //3
          if (t == null)
            return this;
          if (this.linkage == null)
            this.linkage = new ArrayList<SubstanceNucleicAcidSubunitLinkageComponent>();
          this.linkage.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #linkage}, creating it if it does not already exist
         */
        public SubstanceNucleicAcidSubunitLinkageComponent getLinkageFirstRep() { 
          if (getLinkage().isEmpty()) {
            addLinkage();
          }
          return getLinkage().get(0);
        }

        /**
         * @return {@link #sugar} (5.3.6.8.1 Sugar ID (Mandatory).)
         */
        public List<SubstanceNucleicAcidSubunitSugarComponent> getSugar() { 
          if (this.sugar == null)
            this.sugar = new ArrayList<SubstanceNucleicAcidSubunitSugarComponent>();
          return this.sugar;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceNucleicAcidSubunitComponent setSugar(List<SubstanceNucleicAcidSubunitSugarComponent> theSugar) { 
          this.sugar = theSugar;
          return this;
        }

        public boolean hasSugar() { 
          if (this.sugar == null)
            return false;
          for (SubstanceNucleicAcidSubunitSugarComponent item : this.sugar)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceNucleicAcidSubunitSugarComponent addSugar() { //3
          SubstanceNucleicAcidSubunitSugarComponent t = new SubstanceNucleicAcidSubunitSugarComponent();
          if (this.sugar == null)
            this.sugar = new ArrayList<SubstanceNucleicAcidSubunitSugarComponent>();
          this.sugar.add(t);
          return t;
        }

        public SubstanceNucleicAcidSubunitComponent addSugar(SubstanceNucleicAcidSubunitSugarComponent t) { //3
          if (t == null)
            return this;
          if (this.sugar == null)
            this.sugar = new ArrayList<SubstanceNucleicAcidSubunitSugarComponent>();
          this.sugar.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #sugar}, creating it if it does not already exist
         */
        public SubstanceNucleicAcidSubunitSugarComponent getSugarFirstRep() { 
          if (getSugar().isEmpty()) {
            addSugar();
          }
          return getSugar().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("subunit", "integer", "Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.", 0, 1, subunit));
          children.add(new Property("sequence", "string", "Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.", 0, 1, sequence));
          children.add(new Property("length", "integer", "The length of the sequence shall be captured.", 0, 1, length));
          children.add(new Property("sequenceAttachment", "Attachment", "(TBC).", 0, 1, sequenceAttachment));
          children.add(new Property("fivePrime", "CodeableConcept", "The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A separate representation would be redundant.", 0, 1, fivePrime));
          children.add(new Property("threePrime", "CodeableConcept", "The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A separate representation would be redundant.", 0, 1, threePrime));
          children.add(new Property("linkage", "", "The linkages between sugar residues will also be captured.", 0, java.lang.Integer.MAX_VALUE, linkage));
          children.add(new Property("sugar", "", "5.3.6.8.1 Sugar ID (Mandatory).", 0, java.lang.Integer.MAX_VALUE, sugar));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1867548732: /*subunit*/  return new Property("subunit", "integer", "Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.", 0, 1, subunit);
          case 1349547969: /*sequence*/  return new Property("sequence", "string", "Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.", 0, 1, sequence);
          case -1106363674: /*length*/  return new Property("length", "integer", "The length of the sequence shall be captured.", 0, 1, length);
          case 364621764: /*sequenceAttachment*/  return new Property("sequenceAttachment", "Attachment", "(TBC).", 0, 1, sequenceAttachment);
          case -1045091603: /*fivePrime*/  return new Property("fivePrime", "CodeableConcept", "The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A separate representation would be redundant.", 0, 1, fivePrime);
          case -1088032895: /*threePrime*/  return new Property("threePrime", "CodeableConcept", "The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A separate representation would be redundant.", 0, 1, threePrime);
          case 177082053: /*linkage*/  return new Property("linkage", "", "The linkages between sugar residues will also be captured.", 0, java.lang.Integer.MAX_VALUE, linkage);
          case 109792566: /*sugar*/  return new Property("sugar", "", "5.3.6.8.1 Sugar ID (Mandatory).", 0, java.lang.Integer.MAX_VALUE, sugar);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1867548732: /*subunit*/ return this.subunit == null ? new Base[0] : new Base[] {this.subunit}; // IntegerType
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // StringType
        case -1106363674: /*length*/ return this.length == null ? new Base[0] : new Base[] {this.length}; // IntegerType
        case 364621764: /*sequenceAttachment*/ return this.sequenceAttachment == null ? new Base[0] : new Base[] {this.sequenceAttachment}; // Attachment
        case -1045091603: /*fivePrime*/ return this.fivePrime == null ? new Base[0] : new Base[] {this.fivePrime}; // CodeableConcept
        case -1088032895: /*threePrime*/ return this.threePrime == null ? new Base[0] : new Base[] {this.threePrime}; // CodeableConcept
        case 177082053: /*linkage*/ return this.linkage == null ? new Base[0] : this.linkage.toArray(new Base[this.linkage.size()]); // SubstanceNucleicAcidSubunitLinkageComponent
        case 109792566: /*sugar*/ return this.sugar == null ? new Base[0] : this.sugar.toArray(new Base[this.sugar.size()]); // SubstanceNucleicAcidSubunitSugarComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1867548732: // subunit
          this.subunit = castToInteger(value); // IntegerType
          return value;
        case 1349547969: // sequence
          this.sequence = castToString(value); // StringType
          return value;
        case -1106363674: // length
          this.length = castToInteger(value); // IntegerType
          return value;
        case 364621764: // sequenceAttachment
          this.sequenceAttachment = castToAttachment(value); // Attachment
          return value;
        case -1045091603: // fivePrime
          this.fivePrime = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1088032895: // threePrime
          this.threePrime = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 177082053: // linkage
          this.getLinkage().add((SubstanceNucleicAcidSubunitLinkageComponent) value); // SubstanceNucleicAcidSubunitLinkageComponent
          return value;
        case 109792566: // sugar
          this.getSugar().add((SubstanceNucleicAcidSubunitSugarComponent) value); // SubstanceNucleicAcidSubunitSugarComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("subunit")) {
          this.subunit = castToInteger(value); // IntegerType
        } else if (name.equals("sequence")) {
          this.sequence = castToString(value); // StringType
        } else if (name.equals("length")) {
          this.length = castToInteger(value); // IntegerType
        } else if (name.equals("sequenceAttachment")) {
          this.sequenceAttachment = castToAttachment(value); // Attachment
        } else if (name.equals("fivePrime")) {
          this.fivePrime = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("threePrime")) {
          this.threePrime = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("linkage")) {
          this.getLinkage().add((SubstanceNucleicAcidSubunitLinkageComponent) value);
        } else if (name.equals("sugar")) {
          this.getSugar().add((SubstanceNucleicAcidSubunitSugarComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867548732:  return getSubunitElement();
        case 1349547969:  return getSequenceElement();
        case -1106363674:  return getLengthElement();
        case 364621764:  return getSequenceAttachment(); 
        case -1045091603:  return getFivePrime(); 
        case -1088032895:  return getThreePrime(); 
        case 177082053:  return addLinkage(); 
        case 109792566:  return addSugar(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867548732: /*subunit*/ return new String[] {"integer"};
        case 1349547969: /*sequence*/ return new String[] {"string"};
        case -1106363674: /*length*/ return new String[] {"integer"};
        case 364621764: /*sequenceAttachment*/ return new String[] {"Attachment"};
        case -1045091603: /*fivePrime*/ return new String[] {"CodeableConcept"};
        case -1088032895: /*threePrime*/ return new String[] {"CodeableConcept"};
        case 177082053: /*linkage*/ return new String[] {};
        case 109792566: /*sugar*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subunit")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.subunit");
        }
        else if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.sequence");
        }
        else if (name.equals("length")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.length");
        }
        else if (name.equals("sequenceAttachment")) {
          this.sequenceAttachment = new Attachment();
          return this.sequenceAttachment;
        }
        else if (name.equals("fivePrime")) {
          this.fivePrime = new CodeableConcept();
          return this.fivePrime;
        }
        else if (name.equals("threePrime")) {
          this.threePrime = new CodeableConcept();
          return this.threePrime;
        }
        else if (name.equals("linkage")) {
          return addLinkage();
        }
        else if (name.equals("sugar")) {
          return addSugar();
        }
        else
          return super.addChild(name);
      }

      public SubstanceNucleicAcidSubunitComponent copy() {
        SubstanceNucleicAcidSubunitComponent dst = new SubstanceNucleicAcidSubunitComponent();
        copyValues(dst);
        dst.subunit = subunit == null ? null : subunit.copy();
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.length = length == null ? null : length.copy();
        dst.sequenceAttachment = sequenceAttachment == null ? null : sequenceAttachment.copy();
        dst.fivePrime = fivePrime == null ? null : fivePrime.copy();
        dst.threePrime = threePrime == null ? null : threePrime.copy();
        if (linkage != null) {
          dst.linkage = new ArrayList<SubstanceNucleicAcidSubunitLinkageComponent>();
          for (SubstanceNucleicAcidSubunitLinkageComponent i : linkage)
            dst.linkage.add(i.copy());
        };
        if (sugar != null) {
          dst.sugar = new ArrayList<SubstanceNucleicAcidSubunitSugarComponent>();
          for (SubstanceNucleicAcidSubunitSugarComponent i : sugar)
            dst.sugar.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcidSubunitComponent))
          return false;
        SubstanceNucleicAcidSubunitComponent o = (SubstanceNucleicAcidSubunitComponent) other_;
        return compareDeep(subunit, o.subunit, true) && compareDeep(sequence, o.sequence, true) && compareDeep(length, o.length, true)
           && compareDeep(sequenceAttachment, o.sequenceAttachment, true) && compareDeep(fivePrime, o.fivePrime, true)
           && compareDeep(threePrime, o.threePrime, true) && compareDeep(linkage, o.linkage, true) && compareDeep(sugar, o.sugar, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcidSubunitComponent))
          return false;
        SubstanceNucleicAcidSubunitComponent o = (SubstanceNucleicAcidSubunitComponent) other_;
        return compareValues(subunit, o.subunit, true) && compareValues(sequence, o.sequence, true) && compareValues(length, o.length, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(subunit, sequence, length
          , sequenceAttachment, fivePrime, threePrime, linkage, sugar);
      }

  public String fhirType() {
    return "SubstanceNucleicAcid.subunit";

  }

  }

    @Block()
    public static class SubstanceNucleicAcidSubunitLinkageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.
         */
        @Child(name = "connectivity", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified", formalDefinition="The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified." )
        protected StringType connectivity;

        /**
         * Each linkage will be registered as a fragment and have an ID.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Each linkage will be registered as a fragment and have an ID", formalDefinition="Each linkage will be registered as a fragment and have an ID." )
        protected Identifier identifier;

        /**
         * Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage", formalDefinition="Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage." )
        protected StringType name;

        /**
         * Residues shall be captured as described in 5.3.6.8.3.
         */
        @Child(name = "residueSite", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Residues shall be captured as described in 5.3.6.8.3", formalDefinition="Residues shall be captured as described in 5.3.6.8.3." )
        protected StringType residueSite;

        private static final long serialVersionUID = 1392155799L;

    /**
     * Constructor
     */
      public SubstanceNucleicAcidSubunitLinkageComponent() {
        super();
      }

        /**
         * @return {@link #connectivity} (The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.). This is the underlying object with id, value and extensions. The accessor "getConnectivity" gives direct access to the value
         */
        public StringType getConnectivityElement() { 
          if (this.connectivity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitLinkageComponent.connectivity");
            else if (Configuration.doAutoCreate())
              this.connectivity = new StringType(); // bb
          return this.connectivity;
        }

        public boolean hasConnectivityElement() { 
          return this.connectivity != null && !this.connectivity.isEmpty();
        }

        public boolean hasConnectivity() { 
          return this.connectivity != null && !this.connectivity.isEmpty();
        }

        /**
         * @param value {@link #connectivity} (The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.). This is the underlying object with id, value and extensions. The accessor "getConnectivity" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitLinkageComponent setConnectivityElement(StringType value) { 
          this.connectivity = value;
          return this;
        }

        /**
         * @return The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.
         */
        public String getConnectivity() { 
          return this.connectivity == null ? null : this.connectivity.getValue();
        }

        /**
         * @param value The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.
         */
        public SubstanceNucleicAcidSubunitLinkageComponent setConnectivity(String value) { 
          if (Utilities.noString(value))
            this.connectivity = null;
          else {
            if (this.connectivity == null)
              this.connectivity = new StringType();
            this.connectivity.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #identifier} (Each linkage will be registered as a fragment and have an ID.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitLinkageComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Each linkage will be registered as a fragment and have an ID.)
         */
        public SubstanceNucleicAcidSubunitLinkageComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #name} (Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitLinkageComponent.name");
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
         * @param value {@link #name} (Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitLinkageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage.
         */
        public SubstanceNucleicAcidSubunitLinkageComponent setName(String value) { 
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
         * @return {@link #residueSite} (Residues shall be captured as described in 5.3.6.8.3.). This is the underlying object with id, value and extensions. The accessor "getResidueSite" gives direct access to the value
         */
        public StringType getResidueSiteElement() { 
          if (this.residueSite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitLinkageComponent.residueSite");
            else if (Configuration.doAutoCreate())
              this.residueSite = new StringType(); // bb
          return this.residueSite;
        }

        public boolean hasResidueSiteElement() { 
          return this.residueSite != null && !this.residueSite.isEmpty();
        }

        public boolean hasResidueSite() { 
          return this.residueSite != null && !this.residueSite.isEmpty();
        }

        /**
         * @param value {@link #residueSite} (Residues shall be captured as described in 5.3.6.8.3.). This is the underlying object with id, value and extensions. The accessor "getResidueSite" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitLinkageComponent setResidueSiteElement(StringType value) { 
          this.residueSite = value;
          return this;
        }

        /**
         * @return Residues shall be captured as described in 5.3.6.8.3.
         */
        public String getResidueSite() { 
          return this.residueSite == null ? null : this.residueSite.getValue();
        }

        /**
         * @param value Residues shall be captured as described in 5.3.6.8.3.
         */
        public SubstanceNucleicAcidSubunitLinkageComponent setResidueSite(String value) { 
          if (Utilities.noString(value))
            this.residueSite = null;
          else {
            if (this.residueSite == null)
              this.residueSite = new StringType();
            this.residueSite.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("connectivity", "string", "The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.", 0, 1, connectivity));
          children.add(new Property("identifier", "Identifier", "Each linkage will be registered as a fragment and have an ID.", 0, 1, identifier));
          children.add(new Property("name", "string", "Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage.", 0, 1, name));
          children.add(new Property("residueSite", "string", "Residues shall be captured as described in 5.3.6.8.3.", 0, 1, residueSite));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1923312055: /*connectivity*/  return new Property("connectivity", "string", "The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.", 0, 1, connectivity);
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Each linkage will be registered as a fragment and have an ID.", 0, 1, identifier);
          case 3373707: /*name*/  return new Property("name", "string", "Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each linkage.", 0, 1, name);
          case 1547124594: /*residueSite*/  return new Property("residueSite", "string", "Residues shall be captured as described in 5.3.6.8.3.", 0, 1, residueSite);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1923312055: /*connectivity*/ return this.connectivity == null ? new Base[0] : new Base[] {this.connectivity}; // StringType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 1547124594: /*residueSite*/ return this.residueSite == null ? new Base[0] : new Base[] {this.residueSite}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1923312055: // connectivity
          this.connectivity = castToString(value); // StringType
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 1547124594: // residueSite
          this.residueSite = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("connectivity")) {
          this.connectivity = castToString(value); // StringType
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("residueSite")) {
          this.residueSite = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1923312055:  return getConnectivityElement();
        case -1618432855:  return getIdentifier(); 
        case 3373707:  return getNameElement();
        case 1547124594:  return getResidueSiteElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1923312055: /*connectivity*/ return new String[] {"string"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 1547124594: /*residueSite*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("connectivity")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.connectivity");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.name");
        }
        else if (name.equals("residueSite")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.residueSite");
        }
        else
          return super.addChild(name);
      }

      public SubstanceNucleicAcidSubunitLinkageComponent copy() {
        SubstanceNucleicAcidSubunitLinkageComponent dst = new SubstanceNucleicAcidSubunitLinkageComponent();
        copyValues(dst);
        dst.connectivity = connectivity == null ? null : connectivity.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.residueSite = residueSite == null ? null : residueSite.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcidSubunitLinkageComponent))
          return false;
        SubstanceNucleicAcidSubunitLinkageComponent o = (SubstanceNucleicAcidSubunitLinkageComponent) other_;
        return compareDeep(connectivity, o.connectivity, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(name, o.name, true) && compareDeep(residueSite, o.residueSite, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcidSubunitLinkageComponent))
          return false;
        SubstanceNucleicAcidSubunitLinkageComponent o = (SubstanceNucleicAcidSubunitLinkageComponent) other_;
        return compareValues(connectivity, o.connectivity, true) && compareValues(name, o.name, true) && compareValues(residueSite, o.residueSite, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(connectivity, identifier, name
          , residueSite);
      }

  public String fhirType() {
    return "SubstanceNucleicAcid.subunit.linkage";

  }

  }

    @Block()
    public static class SubstanceNucleicAcidSubunitSugarComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The Substance ID of the sugar or sugar-like component that make up the nucleotide.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The Substance ID of the sugar or sugar-like component that make up the nucleotide", formalDefinition="The Substance ID of the sugar or sugar-like component that make up the nucleotide." )
        protected Identifier identifier;

        /**
         * The name of the sugar or sugar-like component that make up the nucleotide.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The name of the sugar or sugar-like component that make up the nucleotide", formalDefinition="The name of the sugar or sugar-like component that make up the nucleotide." )
        protected StringType name;

        /**
         * The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above.
         */
        @Child(name = "residueSite", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above", formalDefinition="The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above." )
        protected StringType residueSite;

        private static final long serialVersionUID = 1933713781L;

    /**
     * Constructor
     */
      public SubstanceNucleicAcidSubunitSugarComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (The Substance ID of the sugar or sugar-like component that make up the nucleotide.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitSugarComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The Substance ID of the sugar or sugar-like component that make up the nucleotide.)
         */
        public SubstanceNucleicAcidSubunitSugarComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #name} (The name of the sugar or sugar-like component that make up the nucleotide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitSugarComponent.name");
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
         * @param value {@link #name} (The name of the sugar or sugar-like component that make up the nucleotide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitSugarComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the sugar or sugar-like component that make up the nucleotide.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the sugar or sugar-like component that make up the nucleotide.
         */
        public SubstanceNucleicAcidSubunitSugarComponent setName(String value) { 
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
         * @return {@link #residueSite} (The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above.). This is the underlying object with id, value and extensions. The accessor "getResidueSite" gives direct access to the value
         */
        public StringType getResidueSiteElement() { 
          if (this.residueSite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceNucleicAcidSubunitSugarComponent.residueSite");
            else if (Configuration.doAutoCreate())
              this.residueSite = new StringType(); // bb
          return this.residueSite;
        }

        public boolean hasResidueSiteElement() { 
          return this.residueSite != null && !this.residueSite.isEmpty();
        }

        public boolean hasResidueSite() { 
          return this.residueSite != null && !this.residueSite.isEmpty();
        }

        /**
         * @param value {@link #residueSite} (The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above.). This is the underlying object with id, value and extensions. The accessor "getResidueSite" gives direct access to the value
         */
        public SubstanceNucleicAcidSubunitSugarComponent setResidueSiteElement(StringType value) { 
          this.residueSite = value;
          return this;
        }

        /**
         * @return The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above.
         */
        public String getResidueSite() { 
          return this.residueSite == null ? null : this.residueSite.getValue();
        }

        /**
         * @param value The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above.
         */
        public SubstanceNucleicAcidSubunitSugarComponent setResidueSite(String value) { 
          if (Utilities.noString(value))
            this.residueSite = null;
          else {
            if (this.residueSite == null)
              this.residueSite = new StringType();
            this.residueSite.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "The Substance ID of the sugar or sugar-like component that make up the nucleotide.", 0, 1, identifier));
          children.add(new Property("name", "string", "The name of the sugar or sugar-like component that make up the nucleotide.", 0, 1, name));
          children.add(new Property("residueSite", "string", "The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above.", 0, 1, residueSite));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "The Substance ID of the sugar or sugar-like component that make up the nucleotide.", 0, 1, identifier);
          case 3373707: /*name*/  return new Property("name", "string", "The name of the sugar or sugar-like component that make up the nucleotide.", 0, 1, name);
          case 1547124594: /*residueSite*/  return new Property("residueSite", "string", "The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3‘direction consistent with the base sequences listed above.", 0, 1, residueSite);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 1547124594: /*residueSite*/ return this.residueSite == null ? new Base[0] : new Base[] {this.residueSite}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 1547124594: // residueSite
          this.residueSite = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("residueSite")) {
          this.residueSite = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3373707:  return getNameElement();
        case 1547124594:  return getResidueSiteElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 1547124594: /*residueSite*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.name");
        }
        else if (name.equals("residueSite")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.residueSite");
        }
        else
          return super.addChild(name);
      }

      public SubstanceNucleicAcidSubunitSugarComponent copy() {
        SubstanceNucleicAcidSubunitSugarComponent dst = new SubstanceNucleicAcidSubunitSugarComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.residueSite = residueSite == null ? null : residueSite.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcidSubunitSugarComponent))
          return false;
        SubstanceNucleicAcidSubunitSugarComponent o = (SubstanceNucleicAcidSubunitSugarComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(residueSite, o.residueSite, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcidSubunitSugarComponent))
          return false;
        SubstanceNucleicAcidSubunitSugarComponent o = (SubstanceNucleicAcidSubunitSugarComponent) other_;
        return compareValues(name, o.name, true) && compareValues(residueSite, o.residueSite, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, name, residueSite
          );
      }

  public String fhirType() {
    return "SubstanceNucleicAcid.subunit.sugar";

  }

  }

    /**
     * The type of the sequence shall be specified based on a controlled vocabulary.
     */
    @Child(name = "sequenceType", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of the sequence shall be specified based on a controlled vocabulary", formalDefinition="The type of the sequence shall be specified based on a controlled vocabulary." )
    protected CodeableConcept sequenceType;

    /**
     * The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit.
     */
    @Child(name = "numberOfSubunits", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit", formalDefinition="The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit." )
    protected IntegerType numberOfSubunits;

    /**
     * The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”.
     */
    @Child(name = "areaOfHybridisation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”", formalDefinition="The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”." )
    protected StringType areaOfHybridisation;

    /**
     * (TBC).
     */
    @Child(name = "oligoNucleotideType", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="(TBC)", formalDefinition="(TBC)." )
    protected CodeableConcept oligoNucleotideType;

    /**
     * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; subunits that have identical sequences will be repeated multiple times.
     */
    @Child(name = "subunit", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; subunits that have identical sequences will be repeated multiple times", formalDefinition="Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; subunits that have identical sequences will be repeated multiple times." )
    protected List<SubstanceNucleicAcidSubunitComponent> subunit;

    private static final long serialVersionUID = -1906822433L;

  /**
   * Constructor
   */
    public SubstanceNucleicAcid() {
      super();
    }

    /**
     * @return {@link #sequenceType} (The type of the sequence shall be specified based on a controlled vocabulary.)
     */
    public CodeableConcept getSequenceType() { 
      if (this.sequenceType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceNucleicAcid.sequenceType");
        else if (Configuration.doAutoCreate())
          this.sequenceType = new CodeableConcept(); // cc
      return this.sequenceType;
    }

    public boolean hasSequenceType() { 
      return this.sequenceType != null && !this.sequenceType.isEmpty();
    }

    /**
     * @param value {@link #sequenceType} (The type of the sequence shall be specified based on a controlled vocabulary.)
     */
    public SubstanceNucleicAcid setSequenceType(CodeableConcept value) { 
      this.sequenceType = value;
      return this;
    }

    /**
     * @return {@link #numberOfSubunits} (The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSubunits" gives direct access to the value
     */
    public IntegerType getNumberOfSubunitsElement() { 
      if (this.numberOfSubunits == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceNucleicAcid.numberOfSubunits");
        else if (Configuration.doAutoCreate())
          this.numberOfSubunits = new IntegerType(); // bb
      return this.numberOfSubunits;
    }

    public boolean hasNumberOfSubunitsElement() { 
      return this.numberOfSubunits != null && !this.numberOfSubunits.isEmpty();
    }

    public boolean hasNumberOfSubunits() { 
      return this.numberOfSubunits != null && !this.numberOfSubunits.isEmpty();
    }

    /**
     * @param value {@link #numberOfSubunits} (The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSubunits" gives direct access to the value
     */
    public SubstanceNucleicAcid setNumberOfSubunitsElement(IntegerType value) { 
      this.numberOfSubunits = value;
      return this;
    }

    /**
     * @return The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit.
     */
    public int getNumberOfSubunits() { 
      return this.numberOfSubunits == null || this.numberOfSubunits.isEmpty() ? 0 : this.numberOfSubunits.getValue();
    }

    /**
     * @param value The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit.
     */
    public SubstanceNucleicAcid setNumberOfSubunits(int value) { 
        if (this.numberOfSubunits == null)
          this.numberOfSubunits = new IntegerType();
        this.numberOfSubunits.setValue(value);
      return this;
    }

    /**
     * @return {@link #areaOfHybridisation} (The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”.). This is the underlying object with id, value and extensions. The accessor "getAreaOfHybridisation" gives direct access to the value
     */
    public StringType getAreaOfHybridisationElement() { 
      if (this.areaOfHybridisation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceNucleicAcid.areaOfHybridisation");
        else if (Configuration.doAutoCreate())
          this.areaOfHybridisation = new StringType(); // bb
      return this.areaOfHybridisation;
    }

    public boolean hasAreaOfHybridisationElement() { 
      return this.areaOfHybridisation != null && !this.areaOfHybridisation.isEmpty();
    }

    public boolean hasAreaOfHybridisation() { 
      return this.areaOfHybridisation != null && !this.areaOfHybridisation.isEmpty();
    }

    /**
     * @param value {@link #areaOfHybridisation} (The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”.). This is the underlying object with id, value and extensions. The accessor "getAreaOfHybridisation" gives direct access to the value
     */
    public SubstanceNucleicAcid setAreaOfHybridisationElement(StringType value) { 
      this.areaOfHybridisation = value;
      return this;
    }

    /**
     * @return The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”.
     */
    public String getAreaOfHybridisation() { 
      return this.areaOfHybridisation == null ? null : this.areaOfHybridisation.getValue();
    }

    /**
     * @param value The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”.
     */
    public SubstanceNucleicAcid setAreaOfHybridisation(String value) { 
      if (Utilities.noString(value))
        this.areaOfHybridisation = null;
      else {
        if (this.areaOfHybridisation == null)
          this.areaOfHybridisation = new StringType();
        this.areaOfHybridisation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #oligoNucleotideType} ((TBC).)
     */
    public CodeableConcept getOligoNucleotideType() { 
      if (this.oligoNucleotideType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceNucleicAcid.oligoNucleotideType");
        else if (Configuration.doAutoCreate())
          this.oligoNucleotideType = new CodeableConcept(); // cc
      return this.oligoNucleotideType;
    }

    public boolean hasOligoNucleotideType() { 
      return this.oligoNucleotideType != null && !this.oligoNucleotideType.isEmpty();
    }

    /**
     * @param value {@link #oligoNucleotideType} ((TBC).)
     */
    public SubstanceNucleicAcid setOligoNucleotideType(CodeableConcept value) { 
      this.oligoNucleotideType = value;
      return this;
    }

    /**
     * @return {@link #subunit} (Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; subunits that have identical sequences will be repeated multiple times.)
     */
    public List<SubstanceNucleicAcidSubunitComponent> getSubunit() { 
      if (this.subunit == null)
        this.subunit = new ArrayList<SubstanceNucleicAcidSubunitComponent>();
      return this.subunit;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceNucleicAcid setSubunit(List<SubstanceNucleicAcidSubunitComponent> theSubunit) { 
      this.subunit = theSubunit;
      return this;
    }

    public boolean hasSubunit() { 
      if (this.subunit == null)
        return false;
      for (SubstanceNucleicAcidSubunitComponent item : this.subunit)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceNucleicAcidSubunitComponent addSubunit() { //3
      SubstanceNucleicAcidSubunitComponent t = new SubstanceNucleicAcidSubunitComponent();
      if (this.subunit == null)
        this.subunit = new ArrayList<SubstanceNucleicAcidSubunitComponent>();
      this.subunit.add(t);
      return t;
    }

    public SubstanceNucleicAcid addSubunit(SubstanceNucleicAcidSubunitComponent t) { //3
      if (t == null)
        return this;
      if (this.subunit == null)
        this.subunit = new ArrayList<SubstanceNucleicAcidSubunitComponent>();
      this.subunit.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subunit}, creating it if it does not already exist
     */
    public SubstanceNucleicAcidSubunitComponent getSubunitFirstRep() { 
      if (getSubunit().isEmpty()) {
        addSubunit();
      }
      return getSubunit().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("sequenceType", "CodeableConcept", "The type of the sequence shall be specified based on a controlled vocabulary.", 0, 1, sequenceType));
        children.add(new Property("numberOfSubunits", "integer", "The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit.", 0, 1, numberOfSubunits));
        children.add(new Property("areaOfHybridisation", "string", "The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”.", 0, 1, areaOfHybridisation));
        children.add(new Property("oligoNucleotideType", "CodeableConcept", "(TBC).", 0, 1, oligoNucleotideType));
        children.add(new Property("subunit", "", "Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; subunits that have identical sequences will be repeated multiple times.", 0, java.lang.Integer.MAX_VALUE, subunit));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 807711387: /*sequenceType*/  return new Property("sequenceType", "CodeableConcept", "The type of the sequence shall be specified based on a controlled vocabulary.", 0, 1, sequenceType);
        case -847111089: /*numberOfSubunits*/  return new Property("numberOfSubunits", "integer", "The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not specified in the reference source, the assumption is that there is 1 subunit.", 0, 1, numberOfSubunits);
        case -617269845: /*areaOfHybridisation*/  return new Property("areaOfHybridisation", "string", "The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” shall be used as separator as follows: “Subunitnumber Residue”.", 0, 1, areaOfHybridisation);
        case -1526251938: /*oligoNucleotideType*/  return new Property("oligoNucleotideType", "CodeableConcept", "(TBC).", 0, 1, oligoNucleotideType);
        case -1867548732: /*subunit*/  return new Property("subunit", "", "Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; subunits that have identical sequences will be repeated multiple times.", 0, java.lang.Integer.MAX_VALUE, subunit);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 807711387: /*sequenceType*/ return this.sequenceType == null ? new Base[0] : new Base[] {this.sequenceType}; // CodeableConcept
        case -847111089: /*numberOfSubunits*/ return this.numberOfSubunits == null ? new Base[0] : new Base[] {this.numberOfSubunits}; // IntegerType
        case -617269845: /*areaOfHybridisation*/ return this.areaOfHybridisation == null ? new Base[0] : new Base[] {this.areaOfHybridisation}; // StringType
        case -1526251938: /*oligoNucleotideType*/ return this.oligoNucleotideType == null ? new Base[0] : new Base[] {this.oligoNucleotideType}; // CodeableConcept
        case -1867548732: /*subunit*/ return this.subunit == null ? new Base[0] : this.subunit.toArray(new Base[this.subunit.size()]); // SubstanceNucleicAcidSubunitComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 807711387: // sequenceType
          this.sequenceType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -847111089: // numberOfSubunits
          this.numberOfSubunits = castToInteger(value); // IntegerType
          return value;
        case -617269845: // areaOfHybridisation
          this.areaOfHybridisation = castToString(value); // StringType
          return value;
        case -1526251938: // oligoNucleotideType
          this.oligoNucleotideType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867548732: // subunit
          this.getSubunit().add((SubstanceNucleicAcidSubunitComponent) value); // SubstanceNucleicAcidSubunitComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequenceType")) {
          this.sequenceType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("numberOfSubunits")) {
          this.numberOfSubunits = castToInteger(value); // IntegerType
        } else if (name.equals("areaOfHybridisation")) {
          this.areaOfHybridisation = castToString(value); // StringType
        } else if (name.equals("oligoNucleotideType")) {
          this.oligoNucleotideType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subunit")) {
          this.getSubunit().add((SubstanceNucleicAcidSubunitComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 807711387:  return getSequenceType(); 
        case -847111089:  return getNumberOfSubunitsElement();
        case -617269845:  return getAreaOfHybridisationElement();
        case -1526251938:  return getOligoNucleotideType(); 
        case -1867548732:  return addSubunit(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 807711387: /*sequenceType*/ return new String[] {"CodeableConcept"};
        case -847111089: /*numberOfSubunits*/ return new String[] {"integer"};
        case -617269845: /*areaOfHybridisation*/ return new String[] {"string"};
        case -1526251938: /*oligoNucleotideType*/ return new String[] {"CodeableConcept"};
        case -1867548732: /*subunit*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequenceType")) {
          this.sequenceType = new CodeableConcept();
          return this.sequenceType;
        }
        else if (name.equals("numberOfSubunits")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.numberOfSubunits");
        }
        else if (name.equals("areaOfHybridisation")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceNucleicAcid.areaOfHybridisation");
        }
        else if (name.equals("oligoNucleotideType")) {
          this.oligoNucleotideType = new CodeableConcept();
          return this.oligoNucleotideType;
        }
        else if (name.equals("subunit")) {
          return addSubunit();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstanceNucleicAcid";

  }

      public SubstanceNucleicAcid copy() {
        SubstanceNucleicAcid dst = new SubstanceNucleicAcid();
        copyValues(dst);
        dst.sequenceType = sequenceType == null ? null : sequenceType.copy();
        dst.numberOfSubunits = numberOfSubunits == null ? null : numberOfSubunits.copy();
        dst.areaOfHybridisation = areaOfHybridisation == null ? null : areaOfHybridisation.copy();
        dst.oligoNucleotideType = oligoNucleotideType == null ? null : oligoNucleotideType.copy();
        if (subunit != null) {
          dst.subunit = new ArrayList<SubstanceNucleicAcidSubunitComponent>();
          for (SubstanceNucleicAcidSubunitComponent i : subunit)
            dst.subunit.add(i.copy());
        };
        return dst;
      }

      protected SubstanceNucleicAcid typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcid))
          return false;
        SubstanceNucleicAcid o = (SubstanceNucleicAcid) other_;
        return compareDeep(sequenceType, o.sequenceType, true) && compareDeep(numberOfSubunits, o.numberOfSubunits, true)
           && compareDeep(areaOfHybridisation, o.areaOfHybridisation, true) && compareDeep(oligoNucleotideType, o.oligoNucleotideType, true)
           && compareDeep(subunit, o.subunit, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceNucleicAcid))
          return false;
        SubstanceNucleicAcid o = (SubstanceNucleicAcid) other_;
        return compareValues(numberOfSubunits, o.numberOfSubunits, true) && compareValues(areaOfHybridisation, o.areaOfHybridisation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequenceType, numberOfSubunits
          , areaOfHybridisation, oligoNucleotideType, subunit);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SubstanceNucleicAcid;
   }


}

