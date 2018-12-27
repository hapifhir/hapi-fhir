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
 * Todo.
 */
@ResourceDef(name="SubstanceReferenceInformation", profile="http://hl7.org/fhir/StructureDefinition/SubstanceReferenceInformation")
public class SubstanceReferenceInformation extends DomainResource {

    @Block()
    public static class SubstanceReferenceInformationGeneComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "geneSequenceOrigin", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept geneSequenceOrigin;

        /**
         * Todo.
         */
        @Child(name = "gene", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept gene;

        /**
         * Todo.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Todo.)
         */
        protected List<DocumentReference> sourceTarget;


        private static final long serialVersionUID = 1615185105L;

    /**
     * Constructor
     */
      public SubstanceReferenceInformationGeneComponent() {
        super();
      }

        /**
         * @return {@link #geneSequenceOrigin} (Todo.)
         */
        public CodeableConcept getGeneSequenceOrigin() { 
          if (this.geneSequenceOrigin == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationGeneComponent.geneSequenceOrigin");
            else if (Configuration.doAutoCreate())
              this.geneSequenceOrigin = new CodeableConcept(); // cc
          return this.geneSequenceOrigin;
        }

        public boolean hasGeneSequenceOrigin() { 
          return this.geneSequenceOrigin != null && !this.geneSequenceOrigin.isEmpty();
        }

        /**
         * @param value {@link #geneSequenceOrigin} (Todo.)
         */
        public SubstanceReferenceInformationGeneComponent setGeneSequenceOrigin(CodeableConcept value) { 
          this.geneSequenceOrigin = value;
          return this;
        }

        /**
         * @return {@link #gene} (Todo.)
         */
        public CodeableConcept getGene() { 
          if (this.gene == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationGeneComponent.gene");
            else if (Configuration.doAutoCreate())
              this.gene = new CodeableConcept(); // cc
          return this.gene;
        }

        public boolean hasGene() { 
          return this.gene != null && !this.gene.isEmpty();
        }

        /**
         * @param value {@link #gene} (Todo.)
         */
        public SubstanceReferenceInformationGeneComponent setGene(CodeableConcept value) { 
          this.gene = value;
          return this;
        }

        /**
         * @return {@link #source} (Todo.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceReferenceInformationGeneComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceReferenceInformationGeneComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("geneSequenceOrigin", "CodeableConcept", "Todo.", 0, 1, geneSequenceOrigin));
          children.add(new Property("gene", "CodeableConcept", "Todo.", 0, 1, gene));
          children.add(new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1089463108: /*geneSequenceOrigin*/  return new Property("geneSequenceOrigin", "CodeableConcept", "Todo.", 0, 1, geneSequenceOrigin);
          case 3169045: /*gene*/  return new Property("gene", "CodeableConcept", "Todo.", 0, 1, gene);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1089463108: /*geneSequenceOrigin*/ return this.geneSequenceOrigin == null ? new Base[0] : new Base[] {this.geneSequenceOrigin}; // CodeableConcept
        case 3169045: /*gene*/ return this.gene == null ? new Base[0] : new Base[] {this.gene}; // CodeableConcept
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1089463108: // geneSequenceOrigin
          this.geneSequenceOrigin = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3169045: // gene
          this.gene = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("geneSequenceOrigin")) {
          this.geneSequenceOrigin = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("gene")) {
          this.gene = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1089463108:  return getGeneSequenceOrigin(); 
        case 3169045:  return getGene(); 
        case -896505829:  return addSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1089463108: /*geneSequenceOrigin*/ return new String[] {"CodeableConcept"};
        case 3169045: /*gene*/ return new String[] {"CodeableConcept"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("geneSequenceOrigin")) {
          this.geneSequenceOrigin = new CodeableConcept();
          return this.geneSequenceOrigin;
        }
        else if (name.equals("gene")) {
          this.gene = new CodeableConcept();
          return this.gene;
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else
          return super.addChild(name);
      }

      public SubstanceReferenceInformationGeneComponent copy() {
        SubstanceReferenceInformationGeneComponent dst = new SubstanceReferenceInformationGeneComponent();
        copyValues(dst);
        dst.geneSequenceOrigin = geneSequenceOrigin == null ? null : geneSequenceOrigin.copy();
        dst.gene = gene == null ? null : gene.copy();
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationGeneComponent))
          return false;
        SubstanceReferenceInformationGeneComponent o = (SubstanceReferenceInformationGeneComponent) other_;
        return compareDeep(geneSequenceOrigin, o.geneSequenceOrigin, true) && compareDeep(gene, o.gene, true)
           && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationGeneComponent))
          return false;
        SubstanceReferenceInformationGeneComponent o = (SubstanceReferenceInformationGeneComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(geneSequenceOrigin, gene, source
          );
      }

  public String fhirType() {
    return "SubstanceReferenceInformation.gene";

  }

  }

    @Block()
    public static class SubstanceReferenceInformationGeneElementComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept type;

        /**
         * Todo.
         */
        @Child(name = "element", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected Identifier element;

        /**
         * Todo.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Todo.)
         */
        protected List<DocumentReference> sourceTarget;


        private static final long serialVersionUID = 2055145950L;

    /**
     * Constructor
     */
      public SubstanceReferenceInformationGeneElementComponent() {
        super();
      }

        /**
         * @return {@link #type} (Todo.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationGeneElementComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Todo.)
         */
        public SubstanceReferenceInformationGeneElementComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #element} (Todo.)
         */
        public Identifier getElement() { 
          if (this.element == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationGeneElementComponent.element");
            else if (Configuration.doAutoCreate())
              this.element = new Identifier(); // cc
          return this.element;
        }

        public boolean hasElement() { 
          return this.element != null && !this.element.isEmpty();
        }

        /**
         * @param value {@link #element} (Todo.)
         */
        public SubstanceReferenceInformationGeneElementComponent setElement(Identifier value) { 
          this.element = value;
          return this;
        }

        /**
         * @return {@link #source} (Todo.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceReferenceInformationGeneElementComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceReferenceInformationGeneElementComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Todo.", 0, 1, type));
          children.add(new Property("element", "Identifier", "Todo.", 0, 1, element));
          children.add(new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Todo.", 0, 1, type);
          case -1662836996: /*element*/  return new Property("element", "Identifier", "Todo.", 0, 1, element);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1662836996: /*element*/ return this.element == null ? new Base[0] : new Base[] {this.element}; // Identifier
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1662836996: // element
          this.element = castToIdentifier(value); // Identifier
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("element")) {
          this.element = castToIdentifier(value); // Identifier
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1662836996:  return getElement(); 
        case -896505829:  return addSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1662836996: /*element*/ return new String[] {"Identifier"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("element")) {
          this.element = new Identifier();
          return this.element;
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else
          return super.addChild(name);
      }

      public SubstanceReferenceInformationGeneElementComponent copy() {
        SubstanceReferenceInformationGeneElementComponent dst = new SubstanceReferenceInformationGeneElementComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.element = element == null ? null : element.copy();
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationGeneElementComponent))
          return false;
        SubstanceReferenceInformationGeneElementComponent o = (SubstanceReferenceInformationGeneElementComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(element, o.element, true) && compareDeep(source, o.source, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationGeneElementComponent))
          return false;
        SubstanceReferenceInformationGeneElementComponent o = (SubstanceReferenceInformationGeneElementComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, element, source);
      }

  public String fhirType() {
    return "SubstanceReferenceInformation.geneElement";

  }

  }

    @Block()
    public static class SubstanceReferenceInformationClassificationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "domain", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept domain;

        /**
         * Todo.
         */
        @Child(name = "classification", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept classification;

        /**
         * Todo.
         */
        @Child(name = "subtype", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<CodeableConcept> subtype;

        /**
         * Todo.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Todo.)
         */
        protected List<DocumentReference> sourceTarget;


        private static final long serialVersionUID = -430084579L;

    /**
     * Constructor
     */
      public SubstanceReferenceInformationClassificationComponent() {
        super();
      }

        /**
         * @return {@link #domain} (Todo.)
         */
        public CodeableConcept getDomain() { 
          if (this.domain == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationClassificationComponent.domain");
            else if (Configuration.doAutoCreate())
              this.domain = new CodeableConcept(); // cc
          return this.domain;
        }

        public boolean hasDomain() { 
          return this.domain != null && !this.domain.isEmpty();
        }

        /**
         * @param value {@link #domain} (Todo.)
         */
        public SubstanceReferenceInformationClassificationComponent setDomain(CodeableConcept value) { 
          this.domain = value;
          return this;
        }

        /**
         * @return {@link #classification} (Todo.)
         */
        public CodeableConcept getClassification() { 
          if (this.classification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationClassificationComponent.classification");
            else if (Configuration.doAutoCreate())
              this.classification = new CodeableConcept(); // cc
          return this.classification;
        }

        public boolean hasClassification() { 
          return this.classification != null && !this.classification.isEmpty();
        }

        /**
         * @param value {@link #classification} (Todo.)
         */
        public SubstanceReferenceInformationClassificationComponent setClassification(CodeableConcept value) { 
          this.classification = value;
          return this;
        }

        /**
         * @return {@link #subtype} (Todo.)
         */
        public List<CodeableConcept> getSubtype() { 
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          return this.subtype;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceReferenceInformationClassificationComponent setSubtype(List<CodeableConcept> theSubtype) { 
          this.subtype = theSubtype;
          return this;
        }

        public boolean hasSubtype() { 
          if (this.subtype == null)
            return false;
          for (CodeableConcept item : this.subtype)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addSubtype() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          this.subtype.add(t);
          return t;
        }

        public SubstanceReferenceInformationClassificationComponent addSubtype(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          this.subtype.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subtype}, creating it if it does not already exist
         */
        public CodeableConcept getSubtypeFirstRep() { 
          if (getSubtype().isEmpty()) {
            addSubtype();
          }
          return getSubtype().get(0);
        }

        /**
         * @return {@link #source} (Todo.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceReferenceInformationClassificationComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceReferenceInformationClassificationComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("domain", "CodeableConcept", "Todo.", 0, 1, domain));
          children.add(new Property("classification", "CodeableConcept", "Todo.", 0, 1, classification));
          children.add(new Property("subtype", "CodeableConcept", "Todo.", 0, java.lang.Integer.MAX_VALUE, subtype));
          children.add(new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1326197564: /*domain*/  return new Property("domain", "CodeableConcept", "Todo.", 0, 1, domain);
          case 382350310: /*classification*/  return new Property("classification", "CodeableConcept", "Todo.", 0, 1, classification);
          case -1867567750: /*subtype*/  return new Property("subtype", "CodeableConcept", "Todo.", 0, java.lang.Integer.MAX_VALUE, subtype);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1326197564: /*domain*/ return this.domain == null ? new Base[0] : new Base[] {this.domain}; // CodeableConcept
        case 382350310: /*classification*/ return this.classification == null ? new Base[0] : new Base[] {this.classification}; // CodeableConcept
        case -1867567750: /*subtype*/ return this.subtype == null ? new Base[0] : this.subtype.toArray(new Base[this.subtype.size()]); // CodeableConcept
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1326197564: // domain
          this.domain = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 382350310: // classification
          this.classification = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867567750: // subtype
          this.getSubtype().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("domain")) {
          this.domain = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("classification")) {
          this.classification = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subtype")) {
          this.getSubtype().add(castToCodeableConcept(value));
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1326197564:  return getDomain(); 
        case 382350310:  return getClassification(); 
        case -1867567750:  return addSubtype(); 
        case -896505829:  return addSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1326197564: /*domain*/ return new String[] {"CodeableConcept"};
        case 382350310: /*classification*/ return new String[] {"CodeableConcept"};
        case -1867567750: /*subtype*/ return new String[] {"CodeableConcept"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("domain")) {
          this.domain = new CodeableConcept();
          return this.domain;
        }
        else if (name.equals("classification")) {
          this.classification = new CodeableConcept();
          return this.classification;
        }
        else if (name.equals("subtype")) {
          return addSubtype();
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else
          return super.addChild(name);
      }

      public SubstanceReferenceInformationClassificationComponent copy() {
        SubstanceReferenceInformationClassificationComponent dst = new SubstanceReferenceInformationClassificationComponent();
        copyValues(dst);
        dst.domain = domain == null ? null : domain.copy();
        dst.classification = classification == null ? null : classification.copy();
        if (subtype != null) {
          dst.subtype = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subtype)
            dst.subtype.add(i.copy());
        };
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationClassificationComponent))
          return false;
        SubstanceReferenceInformationClassificationComponent o = (SubstanceReferenceInformationClassificationComponent) other_;
        return compareDeep(domain, o.domain, true) && compareDeep(classification, o.classification, true)
           && compareDeep(subtype, o.subtype, true) && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationClassificationComponent))
          return false;
        SubstanceReferenceInformationClassificationComponent o = (SubstanceReferenceInformationClassificationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(domain, classification, subtype
          , source);
      }

  public String fhirType() {
    return "SubstanceReferenceInformation.classification";

  }

  }

    @Block()
    public static class SubstanceReferenceInformationTargetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "target", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected Identifier target;

        /**
         * Todo.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept type;

        /**
         * Todo.
         */
        @Child(name = "interaction", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept interaction;

        /**
         * Todo.
         */
        @Child(name = "organism", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept organism;

        /**
         * Todo.
         */
        @Child(name = "organismType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept organismType;

        /**
         * Todo.
         */
        @Child(name = "amount", type = {Quantity.class, Range.class, StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected Type amount;

        /**
         * Todo.
         */
        @Child(name = "amountType", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept amountType;

        /**
         * Todo.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Todo.)
         */
        protected List<DocumentReference> sourceTarget;


        private static final long serialVersionUID = -1682270197L;

    /**
     * Constructor
     */
      public SubstanceReferenceInformationTargetComponent() {
        super();
      }

        /**
         * @return {@link #target} (Todo.)
         */
        public Identifier getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationTargetComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Identifier(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (Todo.)
         */
        public SubstanceReferenceInformationTargetComponent setTarget(Identifier value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #type} (Todo.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationTargetComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Todo.)
         */
        public SubstanceReferenceInformationTargetComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #interaction} (Todo.)
         */
        public CodeableConcept getInteraction() { 
          if (this.interaction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationTargetComponent.interaction");
            else if (Configuration.doAutoCreate())
              this.interaction = new CodeableConcept(); // cc
          return this.interaction;
        }

        public boolean hasInteraction() { 
          return this.interaction != null && !this.interaction.isEmpty();
        }

        /**
         * @param value {@link #interaction} (Todo.)
         */
        public SubstanceReferenceInformationTargetComponent setInteraction(CodeableConcept value) { 
          this.interaction = value;
          return this;
        }

        /**
         * @return {@link #organism} (Todo.)
         */
        public CodeableConcept getOrganism() { 
          if (this.organism == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationTargetComponent.organism");
            else if (Configuration.doAutoCreate())
              this.organism = new CodeableConcept(); // cc
          return this.organism;
        }

        public boolean hasOrganism() { 
          return this.organism != null && !this.organism.isEmpty();
        }

        /**
         * @param value {@link #organism} (Todo.)
         */
        public SubstanceReferenceInformationTargetComponent setOrganism(CodeableConcept value) { 
          this.organism = value;
          return this;
        }

        /**
         * @return {@link #organismType} (Todo.)
         */
        public CodeableConcept getOrganismType() { 
          if (this.organismType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationTargetComponent.organismType");
            else if (Configuration.doAutoCreate())
              this.organismType = new CodeableConcept(); // cc
          return this.organismType;
        }

        public boolean hasOrganismType() { 
          return this.organismType != null && !this.organismType.isEmpty();
        }

        /**
         * @param value {@link #organismType} (Todo.)
         */
        public SubstanceReferenceInformationTargetComponent setOrganismType(CodeableConcept value) { 
          this.organismType = value;
          return this;
        }

        /**
         * @return {@link #amount} (Todo.)
         */
        public Type getAmount() { 
          return this.amount;
        }

        /**
         * @return {@link #amount} (Todo.)
         */
        public Quantity getAmountQuantity() throws FHIRException { 
          if (this.amount == null)
            this.amount = new Quantity();
          if (!(this.amount instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Quantity) this.amount;
        }

        public boolean hasAmountQuantity() { 
          return this != null && this.amount instanceof Quantity;
        }

        /**
         * @return {@link #amount} (Todo.)
         */
        public Range getAmountRange() throws FHIRException { 
          if (this.amount == null)
            this.amount = new Range();
          if (!(this.amount instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Range) this.amount;
        }

        public boolean hasAmountRange() { 
          return this != null && this.amount instanceof Range;
        }

        /**
         * @return {@link #amount} (Todo.)
         */
        public StringType getAmountStringType() throws FHIRException { 
          if (this.amount == null)
            this.amount = new StringType();
          if (!(this.amount instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (StringType) this.amount;
        }

        public boolean hasAmountStringType() { 
          return this != null && this.amount instanceof StringType;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Todo.)
         */
        public SubstanceReferenceInformationTargetComponent setAmount(Type value) { 
          if (value != null && !(value instanceof Quantity || value instanceof Range || value instanceof StringType))
            throw new Error("Not the right type for SubstanceReferenceInformation.target.amount[x]: "+value.fhirType());
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #amountType} (Todo.)
         */
        public CodeableConcept getAmountType() { 
          if (this.amountType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceReferenceInformationTargetComponent.amountType");
            else if (Configuration.doAutoCreate())
              this.amountType = new CodeableConcept(); // cc
          return this.amountType;
        }

        public boolean hasAmountType() { 
          return this.amountType != null && !this.amountType.isEmpty();
        }

        /**
         * @param value {@link #amountType} (Todo.)
         */
        public SubstanceReferenceInformationTargetComponent setAmountType(CodeableConcept value) { 
          this.amountType = value;
          return this;
        }

        /**
         * @return {@link #source} (Todo.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceReferenceInformationTargetComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceReferenceInformationTargetComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("target", "Identifier", "Todo.", 0, 1, target));
          children.add(new Property("type", "CodeableConcept", "Todo.", 0, 1, type));
          children.add(new Property("interaction", "CodeableConcept", "Todo.", 0, 1, interaction));
          children.add(new Property("organism", "CodeableConcept", "Todo.", 0, 1, organism));
          children.add(new Property("organismType", "CodeableConcept", "Todo.", 0, 1, organismType));
          children.add(new Property("amount[x]", "Quantity|Range|string", "Todo.", 0, 1, amount));
          children.add(new Property("amountType", "CodeableConcept", "Todo.", 0, 1, amountType));
          children.add(new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -880905839: /*target*/  return new Property("target", "Identifier", "Todo.", 0, 1, target);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Todo.", 0, 1, type);
          case 1844104722: /*interaction*/  return new Property("interaction", "CodeableConcept", "Todo.", 0, 1, interaction);
          case 1316389074: /*organism*/  return new Property("organism", "CodeableConcept", "Todo.", 0, 1, organism);
          case 988662572: /*organismType*/  return new Property("organismType", "CodeableConcept", "Todo.", 0, 1, organismType);
          case 646780200: /*amount[x]*/  return new Property("amount[x]", "Quantity|Range|string", "Todo.", 0, 1, amount);
          case -1413853096: /*amount*/  return new Property("amount[x]", "Quantity|Range|string", "Todo.", 0, 1, amount);
          case 1664303363: /*amountQuantity*/  return new Property("amount[x]", "Quantity|Range|string", "Todo.", 0, 1, amount);
          case -1223462971: /*amountRange*/  return new Property("amount[x]", "Quantity|Range|string", "Todo.", 0, 1, amount);
          case 773651081: /*amountString*/  return new Property("amount[x]", "Quantity|Range|string", "Todo.", 0, 1, amount);
          case -1424857166: /*amountType*/  return new Property("amountType", "CodeableConcept", "Todo.", 0, 1, amountType);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Todo.", 0, java.lang.Integer.MAX_VALUE, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 1844104722: /*interaction*/ return this.interaction == null ? new Base[0] : new Base[] {this.interaction}; // CodeableConcept
        case 1316389074: /*organism*/ return this.organism == null ? new Base[0] : new Base[] {this.organism}; // CodeableConcept
        case 988662572: /*organismType*/ return this.organismType == null ? new Base[0] : new Base[] {this.organismType}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Type
        case -1424857166: /*amountType*/ return this.amountType == null ? new Base[0] : new Base[] {this.amountType}; // CodeableConcept
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -880905839: // target
          this.target = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1844104722: // interaction
          this.interaction = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1316389074: // organism
          this.organism = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 988662572: // organismType
          this.organismType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToType(value); // Type
          return value;
        case -1424857166: // amountType
          this.amountType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("target")) {
          this.target = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("interaction")) {
          this.interaction = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("organism")) {
          this.organism = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("organismType")) {
          this.organismType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount[x]")) {
          this.amount = castToType(value); // Type
        } else if (name.equals("amountType")) {
          this.amountType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -880905839:  return getTarget(); 
        case 3575610:  return getType(); 
        case 1844104722:  return getInteraction(); 
        case 1316389074:  return getOrganism(); 
        case 988662572:  return getOrganismType(); 
        case 646780200:  return getAmount(); 
        case -1413853096:  return getAmount(); 
        case -1424857166:  return getAmountType(); 
        case -896505829:  return addSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -880905839: /*target*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 1844104722: /*interaction*/ return new String[] {"CodeableConcept"};
        case 1316389074: /*organism*/ return new String[] {"CodeableConcept"};
        case 988662572: /*organismType*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"Quantity", "Range", "string"};
        case -1424857166: /*amountType*/ return new String[] {"CodeableConcept"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("target")) {
          this.target = new Identifier();
          return this.target;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("interaction")) {
          this.interaction = new CodeableConcept();
          return this.interaction;
        }
        else if (name.equals("organism")) {
          this.organism = new CodeableConcept();
          return this.organism;
        }
        else if (name.equals("organismType")) {
          this.organismType = new CodeableConcept();
          return this.organismType;
        }
        else if (name.equals("amountQuantity")) {
          this.amount = new Quantity();
          return this.amount;
        }
        else if (name.equals("amountRange")) {
          this.amount = new Range();
          return this.amount;
        }
        else if (name.equals("amountString")) {
          this.amount = new StringType();
          return this.amount;
        }
        else if (name.equals("amountType")) {
          this.amountType = new CodeableConcept();
          return this.amountType;
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else
          return super.addChild(name);
      }

      public SubstanceReferenceInformationTargetComponent copy() {
        SubstanceReferenceInformationTargetComponent dst = new SubstanceReferenceInformationTargetComponent();
        copyValues(dst);
        dst.target = target == null ? null : target.copy();
        dst.type = type == null ? null : type.copy();
        dst.interaction = interaction == null ? null : interaction.copy();
        dst.organism = organism == null ? null : organism.copy();
        dst.organismType = organismType == null ? null : organismType.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.amountType = amountType == null ? null : amountType.copy();
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationTargetComponent))
          return false;
        SubstanceReferenceInformationTargetComponent o = (SubstanceReferenceInformationTargetComponent) other_;
        return compareDeep(target, o.target, true) && compareDeep(type, o.type, true) && compareDeep(interaction, o.interaction, true)
           && compareDeep(organism, o.organism, true) && compareDeep(organismType, o.organismType, true) && compareDeep(amount, o.amount, true)
           && compareDeep(amountType, o.amountType, true) && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformationTargetComponent))
          return false;
        SubstanceReferenceInformationTargetComponent o = (SubstanceReferenceInformationTargetComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(target, type, interaction
          , organism, organismType, amount, amountType, source);
      }

  public String fhirType() {
    return "SubstanceReferenceInformation.target";

  }

  }

    /**
     * Todo.
     */
    @Child(name = "comment", type = {StringType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected StringType comment;

    /**
     * Todo.
     */
    @Child(name = "gene", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<SubstanceReferenceInformationGeneComponent> gene;

    /**
     * Todo.
     */
    @Child(name = "geneElement", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<SubstanceReferenceInformationGeneElementComponent> geneElement;

    /**
     * Todo.
     */
    @Child(name = "classification", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<SubstanceReferenceInformationClassificationComponent> classification;

    /**
     * Todo.
     */
    @Child(name = "target", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<SubstanceReferenceInformationTargetComponent> target;

    private static final long serialVersionUID = 890303332L;

  /**
   * Constructor
   */
    public SubstanceReferenceInformation() {
      super();
    }

    /**
     * @return {@link #comment} (Todo.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceReferenceInformation.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType(); // bb
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (Todo.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public SubstanceReferenceInformation setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Todo.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Todo.
     */
    public SubstanceReferenceInformation setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #gene} (Todo.)
     */
    public List<SubstanceReferenceInformationGeneComponent> getGene() { 
      if (this.gene == null)
        this.gene = new ArrayList<SubstanceReferenceInformationGeneComponent>();
      return this.gene;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceReferenceInformation setGene(List<SubstanceReferenceInformationGeneComponent> theGene) { 
      this.gene = theGene;
      return this;
    }

    public boolean hasGene() { 
      if (this.gene == null)
        return false;
      for (SubstanceReferenceInformationGeneComponent item : this.gene)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceReferenceInformationGeneComponent addGene() { //3
      SubstanceReferenceInformationGeneComponent t = new SubstanceReferenceInformationGeneComponent();
      if (this.gene == null)
        this.gene = new ArrayList<SubstanceReferenceInformationGeneComponent>();
      this.gene.add(t);
      return t;
    }

    public SubstanceReferenceInformation addGene(SubstanceReferenceInformationGeneComponent t) { //3
      if (t == null)
        return this;
      if (this.gene == null)
        this.gene = new ArrayList<SubstanceReferenceInformationGeneComponent>();
      this.gene.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #gene}, creating it if it does not already exist
     */
    public SubstanceReferenceInformationGeneComponent getGeneFirstRep() { 
      if (getGene().isEmpty()) {
        addGene();
      }
      return getGene().get(0);
    }

    /**
     * @return {@link #geneElement} (Todo.)
     */
    public List<SubstanceReferenceInformationGeneElementComponent> getGeneElement() { 
      if (this.geneElement == null)
        this.geneElement = new ArrayList<SubstanceReferenceInformationGeneElementComponent>();
      return this.geneElement;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceReferenceInformation setGeneElement(List<SubstanceReferenceInformationGeneElementComponent> theGeneElement) { 
      this.geneElement = theGeneElement;
      return this;
    }

    public boolean hasGeneElement() { 
      if (this.geneElement == null)
        return false;
      for (SubstanceReferenceInformationGeneElementComponent item : this.geneElement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceReferenceInformationGeneElementComponent addGeneElement() { //3
      SubstanceReferenceInformationGeneElementComponent t = new SubstanceReferenceInformationGeneElementComponent();
      if (this.geneElement == null)
        this.geneElement = new ArrayList<SubstanceReferenceInformationGeneElementComponent>();
      this.geneElement.add(t);
      return t;
    }

    public SubstanceReferenceInformation addGeneElement(SubstanceReferenceInformationGeneElementComponent t) { //3
      if (t == null)
        return this;
      if (this.geneElement == null)
        this.geneElement = new ArrayList<SubstanceReferenceInformationGeneElementComponent>();
      this.geneElement.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #geneElement}, creating it if it does not already exist
     */
    public SubstanceReferenceInformationGeneElementComponent getGeneElementFirstRep() { 
      if (getGeneElement().isEmpty()) {
        addGeneElement();
      }
      return getGeneElement().get(0);
    }

    /**
     * @return {@link #classification} (Todo.)
     */
    public List<SubstanceReferenceInformationClassificationComponent> getClassification() { 
      if (this.classification == null)
        this.classification = new ArrayList<SubstanceReferenceInformationClassificationComponent>();
      return this.classification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceReferenceInformation setClassification(List<SubstanceReferenceInformationClassificationComponent> theClassification) { 
      this.classification = theClassification;
      return this;
    }

    public boolean hasClassification() { 
      if (this.classification == null)
        return false;
      for (SubstanceReferenceInformationClassificationComponent item : this.classification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceReferenceInformationClassificationComponent addClassification() { //3
      SubstanceReferenceInformationClassificationComponent t = new SubstanceReferenceInformationClassificationComponent();
      if (this.classification == null)
        this.classification = new ArrayList<SubstanceReferenceInformationClassificationComponent>();
      this.classification.add(t);
      return t;
    }

    public SubstanceReferenceInformation addClassification(SubstanceReferenceInformationClassificationComponent t) { //3
      if (t == null)
        return this;
      if (this.classification == null)
        this.classification = new ArrayList<SubstanceReferenceInformationClassificationComponent>();
      this.classification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #classification}, creating it if it does not already exist
     */
    public SubstanceReferenceInformationClassificationComponent getClassificationFirstRep() { 
      if (getClassification().isEmpty()) {
        addClassification();
      }
      return getClassification().get(0);
    }

    /**
     * @return {@link #target} (Todo.)
     */
    public List<SubstanceReferenceInformationTargetComponent> getTarget() { 
      if (this.target == null)
        this.target = new ArrayList<SubstanceReferenceInformationTargetComponent>();
      return this.target;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceReferenceInformation setTarget(List<SubstanceReferenceInformationTargetComponent> theTarget) { 
      this.target = theTarget;
      return this;
    }

    public boolean hasTarget() { 
      if (this.target == null)
        return false;
      for (SubstanceReferenceInformationTargetComponent item : this.target)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceReferenceInformationTargetComponent addTarget() { //3
      SubstanceReferenceInformationTargetComponent t = new SubstanceReferenceInformationTargetComponent();
      if (this.target == null)
        this.target = new ArrayList<SubstanceReferenceInformationTargetComponent>();
      this.target.add(t);
      return t;
    }

    public SubstanceReferenceInformation addTarget(SubstanceReferenceInformationTargetComponent t) { //3
      if (t == null)
        return this;
      if (this.target == null)
        this.target = new ArrayList<SubstanceReferenceInformationTargetComponent>();
      this.target.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #target}, creating it if it does not already exist
     */
    public SubstanceReferenceInformationTargetComponent getTargetFirstRep() { 
      if (getTarget().isEmpty()) {
        addTarget();
      }
      return getTarget().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("comment", "string", "Todo.", 0, 1, comment));
        children.add(new Property("gene", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, gene));
        children.add(new Property("geneElement", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, geneElement));
        children.add(new Property("classification", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, classification));
        children.add(new Property("target", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, target));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 950398559: /*comment*/  return new Property("comment", "string", "Todo.", 0, 1, comment);
        case 3169045: /*gene*/  return new Property("gene", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, gene);
        case -94918105: /*geneElement*/  return new Property("geneElement", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, geneElement);
        case 382350310: /*classification*/  return new Property("classification", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, classification);
        case -880905839: /*target*/  return new Property("target", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, target);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case 3169045: /*gene*/ return this.gene == null ? new Base[0] : this.gene.toArray(new Base[this.gene.size()]); // SubstanceReferenceInformationGeneComponent
        case -94918105: /*geneElement*/ return this.geneElement == null ? new Base[0] : this.geneElement.toArray(new Base[this.geneElement.size()]); // SubstanceReferenceInformationGeneElementComponent
        case 382350310: /*classification*/ return this.classification == null ? new Base[0] : this.classification.toArray(new Base[this.classification.size()]); // SubstanceReferenceInformationClassificationComponent
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // SubstanceReferenceInformationTargetComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        case 3169045: // gene
          this.getGene().add((SubstanceReferenceInformationGeneComponent) value); // SubstanceReferenceInformationGeneComponent
          return value;
        case -94918105: // geneElement
          this.getGeneElement().add((SubstanceReferenceInformationGeneElementComponent) value); // SubstanceReferenceInformationGeneElementComponent
          return value;
        case 382350310: // classification
          this.getClassification().add((SubstanceReferenceInformationClassificationComponent) value); // SubstanceReferenceInformationClassificationComponent
          return value;
        case -880905839: // target
          this.getTarget().add((SubstanceReferenceInformationTargetComponent) value); // SubstanceReferenceInformationTargetComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else if (name.equals("gene")) {
          this.getGene().add((SubstanceReferenceInformationGeneComponent) value);
        } else if (name.equals("geneElement")) {
          this.getGeneElement().add((SubstanceReferenceInformationGeneElementComponent) value);
        } else if (name.equals("classification")) {
          this.getClassification().add((SubstanceReferenceInformationClassificationComponent) value);
        } else if (name.equals("target")) {
          this.getTarget().add((SubstanceReferenceInformationTargetComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 950398559:  return getCommentElement();
        case 3169045:  return addGene(); 
        case -94918105:  return addGeneElement(); 
        case 382350310:  return addClassification(); 
        case -880905839:  return addTarget(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 950398559: /*comment*/ return new String[] {"string"};
        case 3169045: /*gene*/ return new String[] {};
        case -94918105: /*geneElement*/ return new String[] {};
        case 382350310: /*classification*/ return new String[] {};
        case -880905839: /*target*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceReferenceInformation.comment");
        }
        else if (name.equals("gene")) {
          return addGene();
        }
        else if (name.equals("geneElement")) {
          return addGeneElement();
        }
        else if (name.equals("classification")) {
          return addClassification();
        }
        else if (name.equals("target")) {
          return addTarget();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstanceReferenceInformation";

  }

      public SubstanceReferenceInformation copy() {
        SubstanceReferenceInformation dst = new SubstanceReferenceInformation();
        copyValues(dst);
        dst.comment = comment == null ? null : comment.copy();
        if (gene != null) {
          dst.gene = new ArrayList<SubstanceReferenceInformationGeneComponent>();
          for (SubstanceReferenceInformationGeneComponent i : gene)
            dst.gene.add(i.copy());
        };
        if (geneElement != null) {
          dst.geneElement = new ArrayList<SubstanceReferenceInformationGeneElementComponent>();
          for (SubstanceReferenceInformationGeneElementComponent i : geneElement)
            dst.geneElement.add(i.copy());
        };
        if (classification != null) {
          dst.classification = new ArrayList<SubstanceReferenceInformationClassificationComponent>();
          for (SubstanceReferenceInformationClassificationComponent i : classification)
            dst.classification.add(i.copy());
        };
        if (target != null) {
          dst.target = new ArrayList<SubstanceReferenceInformationTargetComponent>();
          for (SubstanceReferenceInformationTargetComponent i : target)
            dst.target.add(i.copy());
        };
        return dst;
      }

      protected SubstanceReferenceInformation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformation))
          return false;
        SubstanceReferenceInformation o = (SubstanceReferenceInformation) other_;
        return compareDeep(comment, o.comment, true) && compareDeep(gene, o.gene, true) && compareDeep(geneElement, o.geneElement, true)
           && compareDeep(classification, o.classification, true) && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceReferenceInformation))
          return false;
        SubstanceReferenceInformation o = (SubstanceReferenceInformation) other_;
        return compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(comment, gene, geneElement
          , classification, target);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SubstanceReferenceInformation;
   }


}

