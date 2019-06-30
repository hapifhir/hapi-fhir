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
 * A SubstanceProtein is defined as a single unit of a linear amino acid sequence, or a combination of subunits that are either covalently linked or have a defined invariant stoichiometric relationship. This includes all synthetic, recombinant and purified SubstanceProteins of defined sequence, whether the use is therapeutic or prophylactic. This set of elements will be used to describe albumins, coagulation factors, cytokines, growth factors, peptide/SubstanceProtein hormones, enzymes, toxins, toxoids, recombinant vaccines, and immunomodulators.
 */
@ResourceDef(name="SubstanceProtein", profile="http://hl7.org/fhir/StructureDefinition/SubstanceProtein")
public class SubstanceProtein extends DomainResource {

    @Block()
    public static class SubstanceProteinSubunitComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         */
        @Child(name = "subunit", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts", formalDefinition="Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts." )
        protected IntegerType subunit;

        /**
         * The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.
         */
        @Child(name = "sequence", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence", formalDefinition="The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence." )
        protected StringType sequence;

        /**
         * Length of linear sequences of amino acids contained in the subunit.
         */
        @Child(name = "length", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Length of linear sequences of amino acids contained in the subunit", formalDefinition="Length of linear sequences of amino acids contained in the subunit." )
        protected IntegerType length;

        /**
         * The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.
         */
        @Child(name = "sequenceAttachment", type = {Attachment.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence", formalDefinition="The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence." )
        protected Attachment sequenceAttachment;

        /**
         * Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.
         */
        @Child(name = "nTerminalModificationId", type = {Identifier.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID", formalDefinition="Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID." )
        protected Identifier nTerminalModificationId;

        /**
         * The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.
         */
        @Child(name = "nTerminalModification", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified", formalDefinition="The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified." )
        protected StringType nTerminalModification;

        /**
         * Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.
         */
        @Child(name = "cTerminalModificationId", type = {Identifier.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID", formalDefinition="Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID." )
        protected Identifier cTerminalModificationId;

        /**
         * The modification at the C-terminal shall be specified.
         */
        @Child(name = "cTerminalModification", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The modification at the C-terminal shall be specified", formalDefinition="The modification at the C-terminal shall be specified." )
        protected StringType cTerminalModification;

        private static final long serialVersionUID = 99973841L;

    /**
     * Constructor
     */
      public SubstanceProteinSubunitComponent() {
        super();
      }

        /**
         * @return {@link #subunit} (Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.). This is the underlying object with id, value and extensions. The accessor "getSubunit" gives direct access to the value
         */
        public IntegerType getSubunitElement() { 
          if (this.subunit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.subunit");
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
         * @param value {@link #subunit} (Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.). This is the underlying object with id, value and extensions. The accessor "getSubunit" gives direct access to the value
         */
        public SubstanceProteinSubunitComponent setSubunitElement(IntegerType value) { 
          this.subunit = value;
          return this;
        }

        /**
         * @return Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         */
        public int getSubunit() { 
          return this.subunit == null || this.subunit.isEmpty() ? 0 : this.subunit.getValue();
        }

        /**
         * @param value Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         */
        public SubstanceProteinSubunitComponent setSubunit(int value) { 
            if (this.subunit == null)
              this.subunit = new IntegerType();
            this.subunit.setValue(value);
          return this;
        }

        /**
         * @return {@link #sequence} (The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public StringType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.sequence");
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
         * @param value {@link #sequence} (The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SubstanceProteinSubunitComponent setSequenceElement(StringType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.
         */
        public String getSequence() { 
          return this.sequence == null ? null : this.sequence.getValue();
        }

        /**
         * @param value The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.
         */
        public SubstanceProteinSubunitComponent setSequence(String value) { 
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
         * @return {@link #length} (Length of linear sequences of amino acids contained in the subunit.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public IntegerType getLengthElement() { 
          if (this.length == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.length");
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
         * @param value {@link #length} (Length of linear sequences of amino acids contained in the subunit.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public SubstanceProteinSubunitComponent setLengthElement(IntegerType value) { 
          this.length = value;
          return this;
        }

        /**
         * @return Length of linear sequences of amino acids contained in the subunit.
         */
        public int getLength() { 
          return this.length == null || this.length.isEmpty() ? 0 : this.length.getValue();
        }

        /**
         * @param value Length of linear sequences of amino acids contained in the subunit.
         */
        public SubstanceProteinSubunitComponent setLength(int value) { 
            if (this.length == null)
              this.length = new IntegerType();
            this.length.setValue(value);
          return this;
        }

        /**
         * @return {@link #sequenceAttachment} (The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.)
         */
        public Attachment getSequenceAttachment() { 
          if (this.sequenceAttachment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.sequenceAttachment");
            else if (Configuration.doAutoCreate())
              this.sequenceAttachment = new Attachment(); // cc
          return this.sequenceAttachment;
        }

        public boolean hasSequenceAttachment() { 
          return this.sequenceAttachment != null && !this.sequenceAttachment.isEmpty();
        }

        /**
         * @param value {@link #sequenceAttachment} (The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.)
         */
        public SubstanceProteinSubunitComponent setSequenceAttachment(Attachment value) { 
          this.sequenceAttachment = value;
          return this;
        }

        /**
         * @return {@link #nTerminalModificationId} (Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.)
         */
        public Identifier getNTerminalModificationId() { 
          if (this.nTerminalModificationId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.nTerminalModificationId");
            else if (Configuration.doAutoCreate())
              this.nTerminalModificationId = new Identifier(); // cc
          return this.nTerminalModificationId;
        }

        public boolean hasNTerminalModificationId() { 
          return this.nTerminalModificationId != null && !this.nTerminalModificationId.isEmpty();
        }

        /**
         * @param value {@link #nTerminalModificationId} (Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.)
         */
        public SubstanceProteinSubunitComponent setNTerminalModificationId(Identifier value) { 
          this.nTerminalModificationId = value;
          return this;
        }

        /**
         * @return {@link #nTerminalModification} (The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.). This is the underlying object with id, value and extensions. The accessor "getNTerminalModification" gives direct access to the value
         */
        public StringType getNTerminalModificationElement() { 
          if (this.nTerminalModification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.nTerminalModification");
            else if (Configuration.doAutoCreate())
              this.nTerminalModification = new StringType(); // bb
          return this.nTerminalModification;
        }

        public boolean hasNTerminalModificationElement() { 
          return this.nTerminalModification != null && !this.nTerminalModification.isEmpty();
        }

        public boolean hasNTerminalModification() { 
          return this.nTerminalModification != null && !this.nTerminalModification.isEmpty();
        }

        /**
         * @param value {@link #nTerminalModification} (The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.). This is the underlying object with id, value and extensions. The accessor "getNTerminalModification" gives direct access to the value
         */
        public SubstanceProteinSubunitComponent setNTerminalModificationElement(StringType value) { 
          this.nTerminalModification = value;
          return this;
        }

        /**
         * @return The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.
         */
        public String getNTerminalModification() { 
          return this.nTerminalModification == null ? null : this.nTerminalModification.getValue();
        }

        /**
         * @param value The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.
         */
        public SubstanceProteinSubunitComponent setNTerminalModification(String value) { 
          if (Utilities.noString(value))
            this.nTerminalModification = null;
          else {
            if (this.nTerminalModification == null)
              this.nTerminalModification = new StringType();
            this.nTerminalModification.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #cTerminalModificationId} (Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.)
         */
        public Identifier getCTerminalModificationId() { 
          if (this.cTerminalModificationId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.cTerminalModificationId");
            else if (Configuration.doAutoCreate())
              this.cTerminalModificationId = new Identifier(); // cc
          return this.cTerminalModificationId;
        }

        public boolean hasCTerminalModificationId() { 
          return this.cTerminalModificationId != null && !this.cTerminalModificationId.isEmpty();
        }

        /**
         * @param value {@link #cTerminalModificationId} (Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.)
         */
        public SubstanceProteinSubunitComponent setCTerminalModificationId(Identifier value) { 
          this.cTerminalModificationId = value;
          return this;
        }

        /**
         * @return {@link #cTerminalModification} (The modification at the C-terminal shall be specified.). This is the underlying object with id, value and extensions. The accessor "getCTerminalModification" gives direct access to the value
         */
        public StringType getCTerminalModificationElement() { 
          if (this.cTerminalModification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceProteinSubunitComponent.cTerminalModification");
            else if (Configuration.doAutoCreate())
              this.cTerminalModification = new StringType(); // bb
          return this.cTerminalModification;
        }

        public boolean hasCTerminalModificationElement() { 
          return this.cTerminalModification != null && !this.cTerminalModification.isEmpty();
        }

        public boolean hasCTerminalModification() { 
          return this.cTerminalModification != null && !this.cTerminalModification.isEmpty();
        }

        /**
         * @param value {@link #cTerminalModification} (The modification at the C-terminal shall be specified.). This is the underlying object with id, value and extensions. The accessor "getCTerminalModification" gives direct access to the value
         */
        public SubstanceProteinSubunitComponent setCTerminalModificationElement(StringType value) { 
          this.cTerminalModification = value;
          return this;
        }

        /**
         * @return The modification at the C-terminal shall be specified.
         */
        public String getCTerminalModification() { 
          return this.cTerminalModification == null ? null : this.cTerminalModification.getValue();
        }

        /**
         * @param value The modification at the C-terminal shall be specified.
         */
        public SubstanceProteinSubunitComponent setCTerminalModification(String value) { 
          if (Utilities.noString(value))
            this.cTerminalModification = null;
          else {
            if (this.cTerminalModification == null)
              this.cTerminalModification = new StringType();
            this.cTerminalModification.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("subunit", "integer", "Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.", 0, 1, subunit));
          children.add(new Property("sequence", "string", "The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.", 0, 1, sequence));
          children.add(new Property("length", "integer", "Length of linear sequences of amino acids contained in the subunit.", 0, 1, length));
          children.add(new Property("sequenceAttachment", "Attachment", "The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.", 0, 1, sequenceAttachment));
          children.add(new Property("nTerminalModificationId", "Identifier", "Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.", 0, 1, nTerminalModificationId));
          children.add(new Property("nTerminalModification", "string", "The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.", 0, 1, nTerminalModification));
          children.add(new Property("cTerminalModificationId", "Identifier", "Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.", 0, 1, cTerminalModificationId));
          children.add(new Property("cTerminalModification", "string", "The modification at the C-terminal shall be specified.", 0, 1, cTerminalModification));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1867548732: /*subunit*/  return new Property("subunit", "integer", "Index of primary sequences of amino acids linked through peptide bonds in order of decreasing length. Sequences of the same length will be ordered by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.", 0, 1, subunit);
          case 1349547969: /*sequence*/  return new Property("sequence", "string", "The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.", 0, 1, sequence);
          case -1106363674: /*length*/  return new Property("length", "integer", "Length of linear sequences of amino acids contained in the subunit.", 0, 1, length);
          case 364621764: /*sequenceAttachment*/  return new Property("sequenceAttachment", "Attachment", "The sequence information shall be provided enumerating the amino acids from N- to C-terminal end using standard single-letter amino acid codes. Uppercase shall be used for L-amino acids and lowercase for D-amino acids. Transcribed SubstanceProteins will always be described using the translated sequence; for synthetic peptide containing amino acids that are not represented with a single letter code an X should be used within the sequence. The modified amino acids will be distinguished by their position in the sequence.", 0, 1, sequenceAttachment);
          case -182796415: /*nTerminalModificationId*/  return new Property("nTerminalModificationId", "Identifier", "Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.", 0, 1, nTerminalModificationId);
          case -1497395258: /*nTerminalModification*/  return new Property("nTerminalModification", "string", "The name of the fragment modified at the N-terminal of the SubstanceProtein shall be specified.", 0, 1, nTerminalModification);
          case -990303818: /*cTerminalModificationId*/  return new Property("cTerminalModificationId", "Identifier", "Unique identifier for molecular fragment modification based on the ISO 11238 Substance ID.", 0, 1, cTerminalModificationId);
          case 472711995: /*cTerminalModification*/  return new Property("cTerminalModification", "string", "The modification at the C-terminal shall be specified.", 0, 1, cTerminalModification);
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
        case -182796415: /*nTerminalModificationId*/ return this.nTerminalModificationId == null ? new Base[0] : new Base[] {this.nTerminalModificationId}; // Identifier
        case -1497395258: /*nTerminalModification*/ return this.nTerminalModification == null ? new Base[0] : new Base[] {this.nTerminalModification}; // StringType
        case -990303818: /*cTerminalModificationId*/ return this.cTerminalModificationId == null ? new Base[0] : new Base[] {this.cTerminalModificationId}; // Identifier
        case 472711995: /*cTerminalModification*/ return this.cTerminalModification == null ? new Base[0] : new Base[] {this.cTerminalModification}; // StringType
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
        case -182796415: // nTerminalModificationId
          this.nTerminalModificationId = castToIdentifier(value); // Identifier
          return value;
        case -1497395258: // nTerminalModification
          this.nTerminalModification = castToString(value); // StringType
          return value;
        case -990303818: // cTerminalModificationId
          this.cTerminalModificationId = castToIdentifier(value); // Identifier
          return value;
        case 472711995: // cTerminalModification
          this.cTerminalModification = castToString(value); // StringType
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
        } else if (name.equals("nTerminalModificationId")) {
          this.nTerminalModificationId = castToIdentifier(value); // Identifier
        } else if (name.equals("nTerminalModification")) {
          this.nTerminalModification = castToString(value); // StringType
        } else if (name.equals("cTerminalModificationId")) {
          this.cTerminalModificationId = castToIdentifier(value); // Identifier
        } else if (name.equals("cTerminalModification")) {
          this.cTerminalModification = castToString(value); // StringType
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
        case -182796415:  return getNTerminalModificationId(); 
        case -1497395258:  return getNTerminalModificationElement();
        case -990303818:  return getCTerminalModificationId(); 
        case 472711995:  return getCTerminalModificationElement();
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
        case -182796415: /*nTerminalModificationId*/ return new String[] {"Identifier"};
        case -1497395258: /*nTerminalModification*/ return new String[] {"string"};
        case -990303818: /*cTerminalModificationId*/ return new String[] {"Identifier"};
        case 472711995: /*cTerminalModification*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subunit")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceProtein.subunit");
        }
        else if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceProtein.sequence");
        }
        else if (name.equals("length")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceProtein.length");
        }
        else if (name.equals("sequenceAttachment")) {
          this.sequenceAttachment = new Attachment();
          return this.sequenceAttachment;
        }
        else if (name.equals("nTerminalModificationId")) {
          this.nTerminalModificationId = new Identifier();
          return this.nTerminalModificationId;
        }
        else if (name.equals("nTerminalModification")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceProtein.nTerminalModification");
        }
        else if (name.equals("cTerminalModificationId")) {
          this.cTerminalModificationId = new Identifier();
          return this.cTerminalModificationId;
        }
        else if (name.equals("cTerminalModification")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceProtein.cTerminalModification");
        }
        else
          return super.addChild(name);
      }

      public SubstanceProteinSubunitComponent copy() {
        SubstanceProteinSubunitComponent dst = new SubstanceProteinSubunitComponent();
        copyValues(dst);
        dst.subunit = subunit == null ? null : subunit.copy();
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.length = length == null ? null : length.copy();
        dst.sequenceAttachment = sequenceAttachment == null ? null : sequenceAttachment.copy();
        dst.nTerminalModificationId = nTerminalModificationId == null ? null : nTerminalModificationId.copy();
        dst.nTerminalModification = nTerminalModification == null ? null : nTerminalModification.copy();
        dst.cTerminalModificationId = cTerminalModificationId == null ? null : cTerminalModificationId.copy();
        dst.cTerminalModification = cTerminalModification == null ? null : cTerminalModification.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceProteinSubunitComponent))
          return false;
        SubstanceProteinSubunitComponent o = (SubstanceProteinSubunitComponent) other_;
        return compareDeep(subunit, o.subunit, true) && compareDeep(sequence, o.sequence, true) && compareDeep(length, o.length, true)
           && compareDeep(sequenceAttachment, o.sequenceAttachment, true) && compareDeep(nTerminalModificationId, o.nTerminalModificationId, true)
           && compareDeep(nTerminalModification, o.nTerminalModification, true) && compareDeep(cTerminalModificationId, o.cTerminalModificationId, true)
           && compareDeep(cTerminalModification, o.cTerminalModification, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceProteinSubunitComponent))
          return false;
        SubstanceProteinSubunitComponent o = (SubstanceProteinSubunitComponent) other_;
        return compareValues(subunit, o.subunit, true) && compareValues(sequence, o.sequence, true) && compareValues(length, o.length, true)
           && compareValues(nTerminalModification, o.nTerminalModification, true) && compareValues(cTerminalModification, o.cTerminalModification, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(subunit, sequence, length
          , sequenceAttachment, nTerminalModificationId, nTerminalModification, cTerminalModificationId
          , cTerminalModification);
      }

  public String fhirType() {
    return "SubstanceProtein.subunit";

  }

  }

    /**
     * The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is available or derivable from a nucleic acid sequence.
     */
    @Child(name = "sequenceType", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is available or derivable from a nucleic acid sequence", formalDefinition="The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is available or derivable from a nucleic acid sequence." )
    protected CodeableConcept sequenceType;

    /**
     * Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable.
     */
    @Child(name = "numberOfSubunits", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable", formalDefinition="Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable." )
    protected IntegerType numberOfSubunits;

    /**
     * The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions.
     */
    @Child(name = "disulfideLinkage", type = {StringType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions", formalDefinition="The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions." )
    protected List<StringType> disulfideLinkage;

    /**
     * This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.
     */
    @Child(name = "subunit", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by decreasing molecular weight; subunits that have identical sequences will be repeated multiple times", formalDefinition="This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by decreasing molecular weight; subunits that have identical sequences will be repeated multiple times." )
    protected List<SubstanceProteinSubunitComponent> subunit;

    private static final long serialVersionUID = 469786856L;

  /**
   * Constructor
   */
    public SubstanceProtein() {
      super();
    }

    /**
     * @return {@link #sequenceType} (The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is available or derivable from a nucleic acid sequence.)
     */
    public CodeableConcept getSequenceType() { 
      if (this.sequenceType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceProtein.sequenceType");
        else if (Configuration.doAutoCreate())
          this.sequenceType = new CodeableConcept(); // cc
      return this.sequenceType;
    }

    public boolean hasSequenceType() { 
      return this.sequenceType != null && !this.sequenceType.isEmpty();
    }

    /**
     * @param value {@link #sequenceType} (The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is available or derivable from a nucleic acid sequence.)
     */
    public SubstanceProtein setSequenceType(CodeableConcept value) { 
      this.sequenceType = value;
      return this;
    }

    /**
     * @return {@link #numberOfSubunits} (Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSubunits" gives direct access to the value
     */
    public IntegerType getNumberOfSubunitsElement() { 
      if (this.numberOfSubunits == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceProtein.numberOfSubunits");
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
     * @param value {@link #numberOfSubunits} (Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable.). This is the underlying object with id, value and extensions. The accessor "getNumberOfSubunits" gives direct access to the value
     */
    public SubstanceProtein setNumberOfSubunitsElement(IntegerType value) { 
      this.numberOfSubunits = value;
      return this;
    }

    /**
     * @return Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable.
     */
    public int getNumberOfSubunits() { 
      return this.numberOfSubunits == null || this.numberOfSubunits.isEmpty() ? 0 : this.numberOfSubunits.getValue();
    }

    /**
     * @param value Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable.
     */
    public SubstanceProtein setNumberOfSubunits(int value) { 
        if (this.numberOfSubunits == null)
          this.numberOfSubunits = new IntegerType();
        this.numberOfSubunits.setValue(value);
      return this;
    }

    /**
     * @return {@link #disulfideLinkage} (The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions.)
     */
    public List<StringType> getDisulfideLinkage() { 
      if (this.disulfideLinkage == null)
        this.disulfideLinkage = new ArrayList<StringType>();
      return this.disulfideLinkage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceProtein setDisulfideLinkage(List<StringType> theDisulfideLinkage) { 
      this.disulfideLinkage = theDisulfideLinkage;
      return this;
    }

    public boolean hasDisulfideLinkage() { 
      if (this.disulfideLinkage == null)
        return false;
      for (StringType item : this.disulfideLinkage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #disulfideLinkage} (The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions.)
     */
    public StringType addDisulfideLinkageElement() {//2 
      StringType t = new StringType();
      if (this.disulfideLinkage == null)
        this.disulfideLinkage = new ArrayList<StringType>();
      this.disulfideLinkage.add(t);
      return t;
    }

    /**
     * @param value {@link #disulfideLinkage} (The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions.)
     */
    public SubstanceProtein addDisulfideLinkage(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.disulfideLinkage == null)
        this.disulfideLinkage = new ArrayList<StringType>();
      this.disulfideLinkage.add(t);
      return this;
    }

    /**
     * @param value {@link #disulfideLinkage} (The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions.)
     */
    public boolean hasDisulfideLinkage(String value) { 
      if (this.disulfideLinkage == null)
        return false;
      for (StringType v : this.disulfideLinkage)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #subunit} (This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.)
     */
    public List<SubstanceProteinSubunitComponent> getSubunit() { 
      if (this.subunit == null)
        this.subunit = new ArrayList<SubstanceProteinSubunitComponent>();
      return this.subunit;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceProtein setSubunit(List<SubstanceProteinSubunitComponent> theSubunit) { 
      this.subunit = theSubunit;
      return this;
    }

    public boolean hasSubunit() { 
      if (this.subunit == null)
        return false;
      for (SubstanceProteinSubunitComponent item : this.subunit)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceProteinSubunitComponent addSubunit() { //3
      SubstanceProteinSubunitComponent t = new SubstanceProteinSubunitComponent();
      if (this.subunit == null)
        this.subunit = new ArrayList<SubstanceProteinSubunitComponent>();
      this.subunit.add(t);
      return t;
    }

    public SubstanceProtein addSubunit(SubstanceProteinSubunitComponent t) { //3
      if (t == null)
        return this;
      if (this.subunit == null)
        this.subunit = new ArrayList<SubstanceProteinSubunitComponent>();
      this.subunit.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subunit}, creating it if it does not already exist
     */
    public SubstanceProteinSubunitComponent getSubunitFirstRep() { 
      if (getSubunit().isEmpty()) {
        addSubunit();
      }
      return getSubunit().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("sequenceType", "CodeableConcept", "The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is available or derivable from a nucleic acid sequence.", 0, 1, sequenceType));
        children.add(new Property("numberOfSubunits", "integer", "Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable.", 0, 1, numberOfSubunits));
        children.add(new Property("disulfideLinkage", "string", "The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions.", 0, java.lang.Integer.MAX_VALUE, disulfideLinkage));
        children.add(new Property("subunit", "", "This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.", 0, java.lang.Integer.MAX_VALUE, subunit));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 807711387: /*sequenceType*/  return new Property("sequenceType", "CodeableConcept", "The SubstanceProtein descriptive elements will only be used when a complete or partial amino acid sequence is available or derivable from a nucleic acid sequence.", 0, 1, sequenceType);
        case -847111089: /*numberOfSubunits*/  return new Property("numberOfSubunits", "integer", "Number of linear sequences of amino acids linked through peptide bonds. The number of subunits constituting the SubstanceProtein shall be described. It is possible that the number of subunits can be variable.", 0, 1, numberOfSubunits);
        case -1996102436: /*disulfideLinkage*/  return new Property("disulfideLinkage", "string", "The disulphide bond between two cysteine residues either on the same subunit or on two different subunits shall be described. The position of the disulfide bonds in the SubstanceProtein shall be listed in increasing order of subunit number and position within subunit followed by the abbreviation of the amino acids involved. The disulfide linkage positions shall actually contain the amino acid Cysteine at the respective positions.", 0, java.lang.Integer.MAX_VALUE, disulfideLinkage);
        case -1867548732: /*subunit*/  return new Property("subunit", "", "This subclause refers to the description of each subunit constituting the SubstanceProtein. A subunit is a linear sequence of amino acids linked through peptide bonds. The Subunit information shall be provided when the finished SubstanceProtein is a complex of multiple sequences; subunits are not used to delineate domains within a single sequence. Subunits are listed in order of decreasing length; sequences of the same length will be ordered by decreasing molecular weight; subunits that have identical sequences will be repeated multiple times.", 0, java.lang.Integer.MAX_VALUE, subunit);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 807711387: /*sequenceType*/ return this.sequenceType == null ? new Base[0] : new Base[] {this.sequenceType}; // CodeableConcept
        case -847111089: /*numberOfSubunits*/ return this.numberOfSubunits == null ? new Base[0] : new Base[] {this.numberOfSubunits}; // IntegerType
        case -1996102436: /*disulfideLinkage*/ return this.disulfideLinkage == null ? new Base[0] : this.disulfideLinkage.toArray(new Base[this.disulfideLinkage.size()]); // StringType
        case -1867548732: /*subunit*/ return this.subunit == null ? new Base[0] : this.subunit.toArray(new Base[this.subunit.size()]); // SubstanceProteinSubunitComponent
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
        case -1996102436: // disulfideLinkage
          this.getDisulfideLinkage().add(castToString(value)); // StringType
          return value;
        case -1867548732: // subunit
          this.getSubunit().add((SubstanceProteinSubunitComponent) value); // SubstanceProteinSubunitComponent
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
        } else if (name.equals("disulfideLinkage")) {
          this.getDisulfideLinkage().add(castToString(value));
        } else if (name.equals("subunit")) {
          this.getSubunit().add((SubstanceProteinSubunitComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 807711387:  return getSequenceType(); 
        case -847111089:  return getNumberOfSubunitsElement();
        case -1996102436:  return addDisulfideLinkageElement();
        case -1867548732:  return addSubunit(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 807711387: /*sequenceType*/ return new String[] {"CodeableConcept"};
        case -847111089: /*numberOfSubunits*/ return new String[] {"integer"};
        case -1996102436: /*disulfideLinkage*/ return new String[] {"string"};
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
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceProtein.numberOfSubunits");
        }
        else if (name.equals("disulfideLinkage")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceProtein.disulfideLinkage");
        }
        else if (name.equals("subunit")) {
          return addSubunit();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstanceProtein";

  }

      public SubstanceProtein copy() {
        SubstanceProtein dst = new SubstanceProtein();
        copyValues(dst);
        dst.sequenceType = sequenceType == null ? null : sequenceType.copy();
        dst.numberOfSubunits = numberOfSubunits == null ? null : numberOfSubunits.copy();
        if (disulfideLinkage != null) {
          dst.disulfideLinkage = new ArrayList<StringType>();
          for (StringType i : disulfideLinkage)
            dst.disulfideLinkage.add(i.copy());
        };
        if (subunit != null) {
          dst.subunit = new ArrayList<SubstanceProteinSubunitComponent>();
          for (SubstanceProteinSubunitComponent i : subunit)
            dst.subunit.add(i.copy());
        };
        return dst;
      }

      protected SubstanceProtein typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceProtein))
          return false;
        SubstanceProtein o = (SubstanceProtein) other_;
        return compareDeep(sequenceType, o.sequenceType, true) && compareDeep(numberOfSubunits, o.numberOfSubunits, true)
           && compareDeep(disulfideLinkage, o.disulfideLinkage, true) && compareDeep(subunit, o.subunit, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceProtein))
          return false;
        SubstanceProtein o = (SubstanceProtein) other_;
        return compareValues(numberOfSubunits, o.numberOfSubunits, true) && compareValues(disulfideLinkage, o.disulfideLinkage, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequenceType, numberOfSubunits
          , disulfideLinkage, subunit);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SubstanceProtein;
   }


}

