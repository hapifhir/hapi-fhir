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
 * Source material shall capture information on the taxonomic and anatomical origins as well as the fraction of a material that can result in or can be modified to form a substance. This set of data elements shall be used to define polymer substances isolated from biological matrices. Taxonomic and anatomical origins shall be described using a controlled vocabulary as required. This information is captured for naturally derived polymers ( . starch) and structurally diverse substances. For Organisms belonging to the Kingdom Plantae the Substance level defines the fresh material of a single species or infraspecies, the Herbal Drug and the Herbal preparation. For Herbal preparations, the fraction information will be captured at the Substance information level and additional information for herbal extracts will be captured at the Specified Substance Group 1 information level. See for further explanation the Substance Class: Structurally Diverse and the herbal annex.
 */
@ResourceDef(name="SubstanceSourceMaterial", profile="http://hl7.org/fhir/StructureDefinition/SubstanceSourceMaterial")
public class SubstanceSourceMaterial extends DomainResource {

    @Block()
    public static class SubstanceSourceMaterialFractionDescriptionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * This element is capturing information about the fraction of a plant part, or human plasma for fractionation.
         */
        @Child(name = "fraction", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="This element is capturing information about the fraction of a plant part, or human plasma for fractionation", formalDefinition="This element is capturing information about the fraction of a plant part, or human plasma for fractionation." )
        protected StringType fraction;

        /**
         * The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts (liquid/dry) is described in Specified Substance Group 1.
         */
        @Child(name = "materialType", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts (liquid/dry) is described in Specified Substance Group 1", formalDefinition="The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts (liquid/dry) is described in Specified Substance Group 1." )
        protected CodeableConcept materialType;

        private static final long serialVersionUID = -1118226733L;

    /**
     * Constructor
     */
      public SubstanceSourceMaterialFractionDescriptionComponent() {
        super();
      }

        /**
         * @return {@link #fraction} (This element is capturing information about the fraction of a plant part, or human plasma for fractionation.). This is the underlying object with id, value and extensions. The accessor "getFraction" gives direct access to the value
         */
        public StringType getFractionElement() { 
          if (this.fraction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialFractionDescriptionComponent.fraction");
            else if (Configuration.doAutoCreate())
              this.fraction = new StringType(); // bb
          return this.fraction;
        }

        public boolean hasFractionElement() { 
          return this.fraction != null && !this.fraction.isEmpty();
        }

        public boolean hasFraction() { 
          return this.fraction != null && !this.fraction.isEmpty();
        }

        /**
         * @param value {@link #fraction} (This element is capturing information about the fraction of a plant part, or human plasma for fractionation.). This is the underlying object with id, value and extensions. The accessor "getFraction" gives direct access to the value
         */
        public SubstanceSourceMaterialFractionDescriptionComponent setFractionElement(StringType value) { 
          this.fraction = value;
          return this;
        }

        /**
         * @return This element is capturing information about the fraction of a plant part, or human plasma for fractionation.
         */
        public String getFraction() { 
          return this.fraction == null ? null : this.fraction.getValue();
        }

        /**
         * @param value This element is capturing information about the fraction of a plant part, or human plasma for fractionation.
         */
        public SubstanceSourceMaterialFractionDescriptionComponent setFraction(String value) { 
          if (Utilities.noString(value))
            this.fraction = null;
          else {
            if (this.fraction == null)
              this.fraction = new StringType();
            this.fraction.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #materialType} (The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts (liquid/dry) is described in Specified Substance Group 1.)
         */
        public CodeableConcept getMaterialType() { 
          if (this.materialType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialFractionDescriptionComponent.materialType");
            else if (Configuration.doAutoCreate())
              this.materialType = new CodeableConcept(); // cc
          return this.materialType;
        }

        public boolean hasMaterialType() { 
          return this.materialType != null && !this.materialType.isEmpty();
        }

        /**
         * @param value {@link #materialType} (The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts (liquid/dry) is described in Specified Substance Group 1.)
         */
        public SubstanceSourceMaterialFractionDescriptionComponent setMaterialType(CodeableConcept value) { 
          this.materialType = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("fraction", "string", "This element is capturing information about the fraction of a plant part, or human plasma for fractionation.", 0, 1, fraction));
          children.add(new Property("materialType", "CodeableConcept", "The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts (liquid/dry) is described in Specified Substance Group 1.", 0, 1, materialType));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1653751294: /*fraction*/  return new Property("fraction", "string", "This element is capturing information about the fraction of a plant part, or human plasma for fractionation.", 0, 1, fraction);
          case -2115601151: /*materialType*/  return new Property("materialType", "CodeableConcept", "The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts (liquid/dry) is described in Specified Substance Group 1.", 0, 1, materialType);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1653751294: /*fraction*/ return this.fraction == null ? new Base[0] : new Base[] {this.fraction}; // StringType
        case -2115601151: /*materialType*/ return this.materialType == null ? new Base[0] : new Base[] {this.materialType}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1653751294: // fraction
          this.fraction = castToString(value); // StringType
          return value;
        case -2115601151: // materialType
          this.materialType = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("fraction")) {
          this.fraction = castToString(value); // StringType
        } else if (name.equals("materialType")) {
          this.materialType = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1653751294:  return getFractionElement();
        case -2115601151:  return getMaterialType(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1653751294: /*fraction*/ return new String[] {"string"};
        case -2115601151: /*materialType*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("fraction")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.fraction");
        }
        else if (name.equals("materialType")) {
          this.materialType = new CodeableConcept();
          return this.materialType;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSourceMaterialFractionDescriptionComponent copy() {
        SubstanceSourceMaterialFractionDescriptionComponent dst = new SubstanceSourceMaterialFractionDescriptionComponent();
        copyValues(dst);
        dst.fraction = fraction == null ? null : fraction.copy();
        dst.materialType = materialType == null ? null : materialType.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialFractionDescriptionComponent))
          return false;
        SubstanceSourceMaterialFractionDescriptionComponent o = (SubstanceSourceMaterialFractionDescriptionComponent) other_;
        return compareDeep(fraction, o.fraction, true) && compareDeep(materialType, o.materialType, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialFractionDescriptionComponent))
          return false;
        SubstanceSourceMaterialFractionDescriptionComponent o = (SubstanceSourceMaterialFractionDescriptionComponent) other_;
        return compareValues(fraction, o.fraction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(fraction, materialType);
      }

  public String fhirType() {
    return "SubstanceSourceMaterial.fractionDescription";

  }

  }

    @Block()
    public static class SubstanceSourceMaterialOrganismComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The family of an organism shall be specified.
         */
        @Child(name = "family", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The family of an organism shall be specified", formalDefinition="The family of an organism shall be specified." )
        protected CodeableConcept family;

        /**
         * The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal scientific name; it is present in names for genera, species and infraspecies.
         */
        @Child(name = "genus", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal scientific name; it is present in names for genera, species and infraspecies", formalDefinition="The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal scientific name; it is present in names for genera, species and infraspecies." )
        protected CodeableConcept genus;

        /**
         * The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is present in names for species and infraspecies.
         */
        @Child(name = "species", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is present in names for species and infraspecies", formalDefinition="The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is present in names for species and infraspecies." )
        protected CodeableConcept species;

        /**
         * The Intraspecific type of an organism shall be specified.
         */
        @Child(name = "intraspecificType", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The Intraspecific type of an organism shall be specified", formalDefinition="The Intraspecific type of an organism shall be specified." )
        protected CodeableConcept intraspecificType;

        /**
         * The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.
         */
        @Child(name = "intraspecificDescription", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention", formalDefinition="The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention." )
        protected StringType intraspecificDescription;

        /**
         * 4.9.13.6.1 Author type (Conditional).
         */
        @Child(name = "author", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="4.9.13.6.1 Author type (Conditional)", formalDefinition="4.9.13.6.1 Author type (Conditional)." )
        protected List<SubstanceSourceMaterialOrganismAuthorComponent> author;

        /**
         * 4.9.13.8.1 Hybrid species maternal organism ID (Optional).
         */
        @Child(name = "hybrid", type = {}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="4.9.13.8.1 Hybrid species maternal organism ID (Optional)", formalDefinition="4.9.13.8.1 Hybrid species maternal organism ID (Optional)." )
        protected SubstanceSourceMaterialOrganismHybridComponent hybrid;

        /**
         * 4.9.13.7.1 Kingdom (Conditional).
         */
        @Child(name = "organismGeneral", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="4.9.13.7.1 Kingdom (Conditional)", formalDefinition="4.9.13.7.1 Kingdom (Conditional)." )
        protected SubstanceSourceMaterialOrganismOrganismGeneralComponent organismGeneral;

        private static final long serialVersionUID = 941648312L;

    /**
     * Constructor
     */
      public SubstanceSourceMaterialOrganismComponent() {
        super();
      }

        /**
         * @return {@link #family} (The family of an organism shall be specified.)
         */
        public CodeableConcept getFamily() { 
          if (this.family == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismComponent.family");
            else if (Configuration.doAutoCreate())
              this.family = new CodeableConcept(); // cc
          return this.family;
        }

        public boolean hasFamily() { 
          return this.family != null && !this.family.isEmpty();
        }

        /**
         * @param value {@link #family} (The family of an organism shall be specified.)
         */
        public SubstanceSourceMaterialOrganismComponent setFamily(CodeableConcept value) { 
          this.family = value;
          return this;
        }

        /**
         * @return {@link #genus} (The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal scientific name; it is present in names for genera, species and infraspecies.)
         */
        public CodeableConcept getGenus() { 
          if (this.genus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismComponent.genus");
            else if (Configuration.doAutoCreate())
              this.genus = new CodeableConcept(); // cc
          return this.genus;
        }

        public boolean hasGenus() { 
          return this.genus != null && !this.genus.isEmpty();
        }

        /**
         * @param value {@link #genus} (The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal scientific name; it is present in names for genera, species and infraspecies.)
         */
        public SubstanceSourceMaterialOrganismComponent setGenus(CodeableConcept value) { 
          this.genus = value;
          return this;
        }

        /**
         * @return {@link #species} (The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is present in names for species and infraspecies.)
         */
        public CodeableConcept getSpecies() { 
          if (this.species == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismComponent.species");
            else if (Configuration.doAutoCreate())
              this.species = new CodeableConcept(); // cc
          return this.species;
        }

        public boolean hasSpecies() { 
          return this.species != null && !this.species.isEmpty();
        }

        /**
         * @param value {@link #species} (The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is present in names for species and infraspecies.)
         */
        public SubstanceSourceMaterialOrganismComponent setSpecies(CodeableConcept value) { 
          this.species = value;
          return this;
        }

        /**
         * @return {@link #intraspecificType} (The Intraspecific type of an organism shall be specified.)
         */
        public CodeableConcept getIntraspecificType() { 
          if (this.intraspecificType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismComponent.intraspecificType");
            else if (Configuration.doAutoCreate())
              this.intraspecificType = new CodeableConcept(); // cc
          return this.intraspecificType;
        }

        public boolean hasIntraspecificType() { 
          return this.intraspecificType != null && !this.intraspecificType.isEmpty();
        }

        /**
         * @param value {@link #intraspecificType} (The Intraspecific type of an organism shall be specified.)
         */
        public SubstanceSourceMaterialOrganismComponent setIntraspecificType(CodeableConcept value) { 
          this.intraspecificType = value;
          return this;
        }

        /**
         * @return {@link #intraspecificDescription} (The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.). This is the underlying object with id, value and extensions. The accessor "getIntraspecificDescription" gives direct access to the value
         */
        public StringType getIntraspecificDescriptionElement() { 
          if (this.intraspecificDescription == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismComponent.intraspecificDescription");
            else if (Configuration.doAutoCreate())
              this.intraspecificDescription = new StringType(); // bb
          return this.intraspecificDescription;
        }

        public boolean hasIntraspecificDescriptionElement() { 
          return this.intraspecificDescription != null && !this.intraspecificDescription.isEmpty();
        }

        public boolean hasIntraspecificDescription() { 
          return this.intraspecificDescription != null && !this.intraspecificDescription.isEmpty();
        }

        /**
         * @param value {@link #intraspecificDescription} (The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.). This is the underlying object with id, value and extensions. The accessor "getIntraspecificDescription" gives direct access to the value
         */
        public SubstanceSourceMaterialOrganismComponent setIntraspecificDescriptionElement(StringType value) { 
          this.intraspecificDescription = value;
          return this;
        }

        /**
         * @return The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.
         */
        public String getIntraspecificDescription() { 
          return this.intraspecificDescription == null ? null : this.intraspecificDescription.getValue();
        }

        /**
         * @param value The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.
         */
        public SubstanceSourceMaterialOrganismComponent setIntraspecificDescription(String value) { 
          if (Utilities.noString(value))
            this.intraspecificDescription = null;
          else {
            if (this.intraspecificDescription == null)
              this.intraspecificDescription = new StringType();
            this.intraspecificDescription.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #author} (4.9.13.6.1 Author type (Conditional).)
         */
        public List<SubstanceSourceMaterialOrganismAuthorComponent> getAuthor() { 
          if (this.author == null)
            this.author = new ArrayList<SubstanceSourceMaterialOrganismAuthorComponent>();
          return this.author;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSourceMaterialOrganismComponent setAuthor(List<SubstanceSourceMaterialOrganismAuthorComponent> theAuthor) { 
          this.author = theAuthor;
          return this;
        }

        public boolean hasAuthor() { 
          if (this.author == null)
            return false;
          for (SubstanceSourceMaterialOrganismAuthorComponent item : this.author)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSourceMaterialOrganismAuthorComponent addAuthor() { //3
          SubstanceSourceMaterialOrganismAuthorComponent t = new SubstanceSourceMaterialOrganismAuthorComponent();
          if (this.author == null)
            this.author = new ArrayList<SubstanceSourceMaterialOrganismAuthorComponent>();
          this.author.add(t);
          return t;
        }

        public SubstanceSourceMaterialOrganismComponent addAuthor(SubstanceSourceMaterialOrganismAuthorComponent t) { //3
          if (t == null)
            return this;
          if (this.author == null)
            this.author = new ArrayList<SubstanceSourceMaterialOrganismAuthorComponent>();
          this.author.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #author}, creating it if it does not already exist
         */
        public SubstanceSourceMaterialOrganismAuthorComponent getAuthorFirstRep() { 
          if (getAuthor().isEmpty()) {
            addAuthor();
          }
          return getAuthor().get(0);
        }

        /**
         * @return {@link #hybrid} (4.9.13.8.1 Hybrid species maternal organism ID (Optional).)
         */
        public SubstanceSourceMaterialOrganismHybridComponent getHybrid() { 
          if (this.hybrid == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismComponent.hybrid");
            else if (Configuration.doAutoCreate())
              this.hybrid = new SubstanceSourceMaterialOrganismHybridComponent(); // cc
          return this.hybrid;
        }

        public boolean hasHybrid() { 
          return this.hybrid != null && !this.hybrid.isEmpty();
        }

        /**
         * @param value {@link #hybrid} (4.9.13.8.1 Hybrid species maternal organism ID (Optional).)
         */
        public SubstanceSourceMaterialOrganismComponent setHybrid(SubstanceSourceMaterialOrganismHybridComponent value) { 
          this.hybrid = value;
          return this;
        }

        /**
         * @return {@link #organismGeneral} (4.9.13.7.1 Kingdom (Conditional).)
         */
        public SubstanceSourceMaterialOrganismOrganismGeneralComponent getOrganismGeneral() { 
          if (this.organismGeneral == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismComponent.organismGeneral");
            else if (Configuration.doAutoCreate())
              this.organismGeneral = new SubstanceSourceMaterialOrganismOrganismGeneralComponent(); // cc
          return this.organismGeneral;
        }

        public boolean hasOrganismGeneral() { 
          return this.organismGeneral != null && !this.organismGeneral.isEmpty();
        }

        /**
         * @param value {@link #organismGeneral} (4.9.13.7.1 Kingdom (Conditional).)
         */
        public SubstanceSourceMaterialOrganismComponent setOrganismGeneral(SubstanceSourceMaterialOrganismOrganismGeneralComponent value) { 
          this.organismGeneral = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("family", "CodeableConcept", "The family of an organism shall be specified.", 0, 1, family));
          children.add(new Property("genus", "CodeableConcept", "The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal scientific name; it is present in names for genera, species and infraspecies.", 0, 1, genus));
          children.add(new Property("species", "CodeableConcept", "The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is present in names for species and infraspecies.", 0, 1, species));
          children.add(new Property("intraspecificType", "CodeableConcept", "The Intraspecific type of an organism shall be specified.", 0, 1, intraspecificType));
          children.add(new Property("intraspecificDescription", "string", "The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.", 0, 1, intraspecificDescription));
          children.add(new Property("author", "", "4.9.13.6.1 Author type (Conditional).", 0, java.lang.Integer.MAX_VALUE, author));
          children.add(new Property("hybrid", "", "4.9.13.8.1 Hybrid species maternal organism ID (Optional).", 0, 1, hybrid));
          children.add(new Property("organismGeneral", "", "4.9.13.7.1 Kingdom (Conditional).", 0, 1, organismGeneral));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1281860764: /*family*/  return new Property("family", "CodeableConcept", "The family of an organism shall be specified.", 0, 1, family);
          case 98241006: /*genus*/  return new Property("genus", "CodeableConcept", "The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal scientific name; it is present in names for genera, species and infraspecies.", 0, 1, genus);
          case -2008465092: /*species*/  return new Property("species", "CodeableConcept", "The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is present in names for species and infraspecies.", 0, 1, species);
          case 1717161194: /*intraspecificType*/  return new Property("intraspecificType", "CodeableConcept", "The Intraspecific type of an organism shall be specified.", 0, 1, intraspecificType);
          case -1473085364: /*intraspecificDescription*/  return new Property("intraspecificDescription", "string", "The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.", 0, 1, intraspecificDescription);
          case -1406328437: /*author*/  return new Property("author", "", "4.9.13.6.1 Author type (Conditional).", 0, java.lang.Integer.MAX_VALUE, author);
          case -1202757124: /*hybrid*/  return new Property("hybrid", "", "4.9.13.8.1 Hybrid species maternal organism ID (Optional).", 0, 1, hybrid);
          case -865996874: /*organismGeneral*/  return new Property("organismGeneral", "", "4.9.13.7.1 Kingdom (Conditional).", 0, 1, organismGeneral);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1281860764: /*family*/ return this.family == null ? new Base[0] : new Base[] {this.family}; // CodeableConcept
        case 98241006: /*genus*/ return this.genus == null ? new Base[0] : new Base[] {this.genus}; // CodeableConcept
        case -2008465092: /*species*/ return this.species == null ? new Base[0] : new Base[] {this.species}; // CodeableConcept
        case 1717161194: /*intraspecificType*/ return this.intraspecificType == null ? new Base[0] : new Base[] {this.intraspecificType}; // CodeableConcept
        case -1473085364: /*intraspecificDescription*/ return this.intraspecificDescription == null ? new Base[0] : new Base[] {this.intraspecificDescription}; // StringType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : this.author.toArray(new Base[this.author.size()]); // SubstanceSourceMaterialOrganismAuthorComponent
        case -1202757124: /*hybrid*/ return this.hybrid == null ? new Base[0] : new Base[] {this.hybrid}; // SubstanceSourceMaterialOrganismHybridComponent
        case -865996874: /*organismGeneral*/ return this.organismGeneral == null ? new Base[0] : new Base[] {this.organismGeneral}; // SubstanceSourceMaterialOrganismOrganismGeneralComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1281860764: // family
          this.family = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 98241006: // genus
          this.genus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2008465092: // species
          this.species = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1717161194: // intraspecificType
          this.intraspecificType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1473085364: // intraspecificDescription
          this.intraspecificDescription = castToString(value); // StringType
          return value;
        case -1406328437: // author
          this.getAuthor().add((SubstanceSourceMaterialOrganismAuthorComponent) value); // SubstanceSourceMaterialOrganismAuthorComponent
          return value;
        case -1202757124: // hybrid
          this.hybrid = (SubstanceSourceMaterialOrganismHybridComponent) value; // SubstanceSourceMaterialOrganismHybridComponent
          return value;
        case -865996874: // organismGeneral
          this.organismGeneral = (SubstanceSourceMaterialOrganismOrganismGeneralComponent) value; // SubstanceSourceMaterialOrganismOrganismGeneralComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("family")) {
          this.family = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("genus")) {
          this.genus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("species")) {
          this.species = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("intraspecificType")) {
          this.intraspecificType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("intraspecificDescription")) {
          this.intraspecificDescription = castToString(value); // StringType
        } else if (name.equals("author")) {
          this.getAuthor().add((SubstanceSourceMaterialOrganismAuthorComponent) value);
        } else if (name.equals("hybrid")) {
          this.hybrid = (SubstanceSourceMaterialOrganismHybridComponent) value; // SubstanceSourceMaterialOrganismHybridComponent
        } else if (name.equals("organismGeneral")) {
          this.organismGeneral = (SubstanceSourceMaterialOrganismOrganismGeneralComponent) value; // SubstanceSourceMaterialOrganismOrganismGeneralComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1281860764:  return getFamily(); 
        case 98241006:  return getGenus(); 
        case -2008465092:  return getSpecies(); 
        case 1717161194:  return getIntraspecificType(); 
        case -1473085364:  return getIntraspecificDescriptionElement();
        case -1406328437:  return addAuthor(); 
        case -1202757124:  return getHybrid(); 
        case -865996874:  return getOrganismGeneral(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1281860764: /*family*/ return new String[] {"CodeableConcept"};
        case 98241006: /*genus*/ return new String[] {"CodeableConcept"};
        case -2008465092: /*species*/ return new String[] {"CodeableConcept"};
        case 1717161194: /*intraspecificType*/ return new String[] {"CodeableConcept"};
        case -1473085364: /*intraspecificDescription*/ return new String[] {"string"};
        case -1406328437: /*author*/ return new String[] {};
        case -1202757124: /*hybrid*/ return new String[] {};
        case -865996874: /*organismGeneral*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("family")) {
          this.family = new CodeableConcept();
          return this.family;
        }
        else if (name.equals("genus")) {
          this.genus = new CodeableConcept();
          return this.genus;
        }
        else if (name.equals("species")) {
          this.species = new CodeableConcept();
          return this.species;
        }
        else if (name.equals("intraspecificType")) {
          this.intraspecificType = new CodeableConcept();
          return this.intraspecificType;
        }
        else if (name.equals("intraspecificDescription")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.intraspecificDescription");
        }
        else if (name.equals("author")) {
          return addAuthor();
        }
        else if (name.equals("hybrid")) {
          this.hybrid = new SubstanceSourceMaterialOrganismHybridComponent();
          return this.hybrid;
        }
        else if (name.equals("organismGeneral")) {
          this.organismGeneral = new SubstanceSourceMaterialOrganismOrganismGeneralComponent();
          return this.organismGeneral;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSourceMaterialOrganismComponent copy() {
        SubstanceSourceMaterialOrganismComponent dst = new SubstanceSourceMaterialOrganismComponent();
        copyValues(dst);
        dst.family = family == null ? null : family.copy();
        dst.genus = genus == null ? null : genus.copy();
        dst.species = species == null ? null : species.copy();
        dst.intraspecificType = intraspecificType == null ? null : intraspecificType.copy();
        dst.intraspecificDescription = intraspecificDescription == null ? null : intraspecificDescription.copy();
        if (author != null) {
          dst.author = new ArrayList<SubstanceSourceMaterialOrganismAuthorComponent>();
          for (SubstanceSourceMaterialOrganismAuthorComponent i : author)
            dst.author.add(i.copy());
        };
        dst.hybrid = hybrid == null ? null : hybrid.copy();
        dst.organismGeneral = organismGeneral == null ? null : organismGeneral.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismComponent))
          return false;
        SubstanceSourceMaterialOrganismComponent o = (SubstanceSourceMaterialOrganismComponent) other_;
        return compareDeep(family, o.family, true) && compareDeep(genus, o.genus, true) && compareDeep(species, o.species, true)
           && compareDeep(intraspecificType, o.intraspecificType, true) && compareDeep(intraspecificDescription, o.intraspecificDescription, true)
           && compareDeep(author, o.author, true) && compareDeep(hybrid, o.hybrid, true) && compareDeep(organismGeneral, o.organismGeneral, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismComponent))
          return false;
        SubstanceSourceMaterialOrganismComponent o = (SubstanceSourceMaterialOrganismComponent) other_;
        return compareValues(intraspecificDescription, o.intraspecificDescription, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(family, genus, species, intraspecificType
          , intraspecificDescription, author, hybrid, organismGeneral);
      }

  public String fhirType() {
    return "SubstanceSourceMaterial.organism";

  }

  }

    @Block()
    public static class SubstanceSourceMaterialOrganismAuthorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of author of an organism species shall be specified. The parenthetical author of an organism species refers to the first author who published the plant/animal name (of any rank). The primary author of an organism species refers to the first author(s), who validly published the plant/animal name.
         */
        @Child(name = "authorType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of author of an organism species shall be specified. The parenthetical author of an organism species refers to the first author who published the plant/animal name (of any rank). The primary author of an organism species refers to the first author(s), who validly published the plant/animal name", formalDefinition="The type of author of an organism species shall be specified. The parenthetical author of an organism species refers to the first author who published the plant/animal name (of any rank). The primary author of an organism species refers to the first author(s), who validly published the plant/animal name." )
        protected CodeableConcept authorType;

        /**
         * The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank).
         */
        @Child(name = "authorDescription", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank)", formalDefinition="The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank)." )
        protected StringType authorDescription;

        private static final long serialVersionUID = 1429770120L;

    /**
     * Constructor
     */
      public SubstanceSourceMaterialOrganismAuthorComponent() {
        super();
      }

        /**
         * @return {@link #authorType} (The type of author of an organism species shall be specified. The parenthetical author of an organism species refers to the first author who published the plant/animal name (of any rank). The primary author of an organism species refers to the first author(s), who validly published the plant/animal name.)
         */
        public CodeableConcept getAuthorType() { 
          if (this.authorType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismAuthorComponent.authorType");
            else if (Configuration.doAutoCreate())
              this.authorType = new CodeableConcept(); // cc
          return this.authorType;
        }

        public boolean hasAuthorType() { 
          return this.authorType != null && !this.authorType.isEmpty();
        }

        /**
         * @param value {@link #authorType} (The type of author of an organism species shall be specified. The parenthetical author of an organism species refers to the first author who published the plant/animal name (of any rank). The primary author of an organism species refers to the first author(s), who validly published the plant/animal name.)
         */
        public SubstanceSourceMaterialOrganismAuthorComponent setAuthorType(CodeableConcept value) { 
          this.authorType = value;
          return this;
        }

        /**
         * @return {@link #authorDescription} (The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank).). This is the underlying object with id, value and extensions. The accessor "getAuthorDescription" gives direct access to the value
         */
        public StringType getAuthorDescriptionElement() { 
          if (this.authorDescription == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismAuthorComponent.authorDescription");
            else if (Configuration.doAutoCreate())
              this.authorDescription = new StringType(); // bb
          return this.authorDescription;
        }

        public boolean hasAuthorDescriptionElement() { 
          return this.authorDescription != null && !this.authorDescription.isEmpty();
        }

        public boolean hasAuthorDescription() { 
          return this.authorDescription != null && !this.authorDescription.isEmpty();
        }

        /**
         * @param value {@link #authorDescription} (The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank).). This is the underlying object with id, value and extensions. The accessor "getAuthorDescription" gives direct access to the value
         */
        public SubstanceSourceMaterialOrganismAuthorComponent setAuthorDescriptionElement(StringType value) { 
          this.authorDescription = value;
          return this;
        }

        /**
         * @return The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank).
         */
        public String getAuthorDescription() { 
          return this.authorDescription == null ? null : this.authorDescription.getValue();
        }

        /**
         * @param value The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank).
         */
        public SubstanceSourceMaterialOrganismAuthorComponent setAuthorDescription(String value) { 
          if (Utilities.noString(value))
            this.authorDescription = null;
          else {
            if (this.authorDescription == null)
              this.authorDescription = new StringType();
            this.authorDescription.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("authorType", "CodeableConcept", "The type of author of an organism species shall be specified. The parenthetical author of an organism species refers to the first author who published the plant/animal name (of any rank). The primary author of an organism species refers to the first author(s), who validly published the plant/animal name.", 0, 1, authorType));
          children.add(new Property("authorDescription", "string", "The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank).", 0, 1, authorDescription));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1501337755: /*authorType*/  return new Property("authorType", "CodeableConcept", "The type of author of an organism species shall be specified. The parenthetical author of an organism species refers to the first author who published the plant/animal name (of any rank). The primary author of an organism species refers to the first author(s), who validly published the plant/animal name.", 0, 1, authorType);
          case -166185615: /*authorDescription*/  return new Property("authorDescription", "string", "The author of an organism species shall be specified. The author year of an organism shall also be specified when applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank).", 0, 1, authorDescription);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1501337755: /*authorType*/ return this.authorType == null ? new Base[0] : new Base[] {this.authorType}; // CodeableConcept
        case -166185615: /*authorDescription*/ return this.authorDescription == null ? new Base[0] : new Base[] {this.authorDescription}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1501337755: // authorType
          this.authorType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -166185615: // authorDescription
          this.authorDescription = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("authorType")) {
          this.authorType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("authorDescription")) {
          this.authorDescription = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1501337755:  return getAuthorType(); 
        case -166185615:  return getAuthorDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1501337755: /*authorType*/ return new String[] {"CodeableConcept"};
        case -166185615: /*authorDescription*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("authorType")) {
          this.authorType = new CodeableConcept();
          return this.authorType;
        }
        else if (name.equals("authorDescription")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.authorDescription");
        }
        else
          return super.addChild(name);
      }

      public SubstanceSourceMaterialOrganismAuthorComponent copy() {
        SubstanceSourceMaterialOrganismAuthorComponent dst = new SubstanceSourceMaterialOrganismAuthorComponent();
        copyValues(dst);
        dst.authorType = authorType == null ? null : authorType.copy();
        dst.authorDescription = authorDescription == null ? null : authorDescription.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismAuthorComponent))
          return false;
        SubstanceSourceMaterialOrganismAuthorComponent o = (SubstanceSourceMaterialOrganismAuthorComponent) other_;
        return compareDeep(authorType, o.authorType, true) && compareDeep(authorDescription, o.authorDescription, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismAuthorComponent))
          return false;
        SubstanceSourceMaterialOrganismAuthorComponent o = (SubstanceSourceMaterialOrganismAuthorComponent) other_;
        return compareValues(authorDescription, o.authorDescription, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(authorType, authorDescription
          );
      }

  public String fhirType() {
    return "SubstanceSourceMaterial.organism.author";

  }

  }

    @Block()
    public static class SubstanceSourceMaterialOrganismHybridComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.
         */
        @Child(name = "maternalOrganismId", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal", formalDefinition="The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal." )
        protected StringType maternalOrganismId;

        /**
         * The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.
         */
        @Child(name = "maternalOrganismName", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal", formalDefinition="The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal." )
        protected StringType maternalOrganismName;

        /**
         * The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary.
         */
        @Child(name = "paternalOrganismId", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary", formalDefinition="The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary." )
        protected StringType paternalOrganismId;

        /**
         * The name of the paternal species constituting the hybrid organism shall be specified.
         */
        @Child(name = "paternalOrganismName", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The name of the paternal species constituting the hybrid organism shall be specified", formalDefinition="The name of the paternal species constituting the hybrid organism shall be specified." )
        protected StringType paternalOrganismName;

        /**
         * The hybrid type of an organism shall be specified.
         */
        @Child(name = "hybridType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The hybrid type of an organism shall be specified", formalDefinition="The hybrid type of an organism shall be specified." )
        protected CodeableConcept hybridType;

        private static final long serialVersionUID = 1981189787L;

    /**
     * Constructor
     */
      public SubstanceSourceMaterialOrganismHybridComponent() {
        super();
      }

        /**
         * @return {@link #maternalOrganismId} (The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.). This is the underlying object with id, value and extensions. The accessor "getMaternalOrganismId" gives direct access to the value
         */
        public StringType getMaternalOrganismIdElement() { 
          if (this.maternalOrganismId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismHybridComponent.maternalOrganismId");
            else if (Configuration.doAutoCreate())
              this.maternalOrganismId = new StringType(); // bb
          return this.maternalOrganismId;
        }

        public boolean hasMaternalOrganismIdElement() { 
          return this.maternalOrganismId != null && !this.maternalOrganismId.isEmpty();
        }

        public boolean hasMaternalOrganismId() { 
          return this.maternalOrganismId != null && !this.maternalOrganismId.isEmpty();
        }

        /**
         * @param value {@link #maternalOrganismId} (The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.). This is the underlying object with id, value and extensions. The accessor "getMaternalOrganismId" gives direct access to the value
         */
        public SubstanceSourceMaterialOrganismHybridComponent setMaternalOrganismIdElement(StringType value) { 
          this.maternalOrganismId = value;
          return this;
        }

        /**
         * @return The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.
         */
        public String getMaternalOrganismId() { 
          return this.maternalOrganismId == null ? null : this.maternalOrganismId.getValue();
        }

        /**
         * @param value The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.
         */
        public SubstanceSourceMaterialOrganismHybridComponent setMaternalOrganismId(String value) { 
          if (Utilities.noString(value))
            this.maternalOrganismId = null;
          else {
            if (this.maternalOrganismId == null)
              this.maternalOrganismId = new StringType();
            this.maternalOrganismId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #maternalOrganismName} (The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.). This is the underlying object with id, value and extensions. The accessor "getMaternalOrganismName" gives direct access to the value
         */
        public StringType getMaternalOrganismNameElement() { 
          if (this.maternalOrganismName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismHybridComponent.maternalOrganismName");
            else if (Configuration.doAutoCreate())
              this.maternalOrganismName = new StringType(); // bb
          return this.maternalOrganismName;
        }

        public boolean hasMaternalOrganismNameElement() { 
          return this.maternalOrganismName != null && !this.maternalOrganismName.isEmpty();
        }

        public boolean hasMaternalOrganismName() { 
          return this.maternalOrganismName != null && !this.maternalOrganismName.isEmpty();
        }

        /**
         * @param value {@link #maternalOrganismName} (The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.). This is the underlying object with id, value and extensions. The accessor "getMaternalOrganismName" gives direct access to the value
         */
        public SubstanceSourceMaterialOrganismHybridComponent setMaternalOrganismNameElement(StringType value) { 
          this.maternalOrganismName = value;
          return this;
        }

        /**
         * @return The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.
         */
        public String getMaternalOrganismName() { 
          return this.maternalOrganismName == null ? null : this.maternalOrganismName.getValue();
        }

        /**
         * @param value The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.
         */
        public SubstanceSourceMaterialOrganismHybridComponent setMaternalOrganismName(String value) { 
          if (Utilities.noString(value))
            this.maternalOrganismName = null;
          else {
            if (this.maternalOrganismName == null)
              this.maternalOrganismName = new StringType();
            this.maternalOrganismName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #paternalOrganismId} (The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary.). This is the underlying object with id, value and extensions. The accessor "getPaternalOrganismId" gives direct access to the value
         */
        public StringType getPaternalOrganismIdElement() { 
          if (this.paternalOrganismId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismHybridComponent.paternalOrganismId");
            else if (Configuration.doAutoCreate())
              this.paternalOrganismId = new StringType(); // bb
          return this.paternalOrganismId;
        }

        public boolean hasPaternalOrganismIdElement() { 
          return this.paternalOrganismId != null && !this.paternalOrganismId.isEmpty();
        }

        public boolean hasPaternalOrganismId() { 
          return this.paternalOrganismId != null && !this.paternalOrganismId.isEmpty();
        }

        /**
         * @param value {@link #paternalOrganismId} (The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary.). This is the underlying object with id, value and extensions. The accessor "getPaternalOrganismId" gives direct access to the value
         */
        public SubstanceSourceMaterialOrganismHybridComponent setPaternalOrganismIdElement(StringType value) { 
          this.paternalOrganismId = value;
          return this;
        }

        /**
         * @return The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary.
         */
        public String getPaternalOrganismId() { 
          return this.paternalOrganismId == null ? null : this.paternalOrganismId.getValue();
        }

        /**
         * @param value The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary.
         */
        public SubstanceSourceMaterialOrganismHybridComponent setPaternalOrganismId(String value) { 
          if (Utilities.noString(value))
            this.paternalOrganismId = null;
          else {
            if (this.paternalOrganismId == null)
              this.paternalOrganismId = new StringType();
            this.paternalOrganismId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #paternalOrganismName} (The name of the paternal species constituting the hybrid organism shall be specified.). This is the underlying object with id, value and extensions. The accessor "getPaternalOrganismName" gives direct access to the value
         */
        public StringType getPaternalOrganismNameElement() { 
          if (this.paternalOrganismName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismHybridComponent.paternalOrganismName");
            else if (Configuration.doAutoCreate())
              this.paternalOrganismName = new StringType(); // bb
          return this.paternalOrganismName;
        }

        public boolean hasPaternalOrganismNameElement() { 
          return this.paternalOrganismName != null && !this.paternalOrganismName.isEmpty();
        }

        public boolean hasPaternalOrganismName() { 
          return this.paternalOrganismName != null && !this.paternalOrganismName.isEmpty();
        }

        /**
         * @param value {@link #paternalOrganismName} (The name of the paternal species constituting the hybrid organism shall be specified.). This is the underlying object with id, value and extensions. The accessor "getPaternalOrganismName" gives direct access to the value
         */
        public SubstanceSourceMaterialOrganismHybridComponent setPaternalOrganismNameElement(StringType value) { 
          this.paternalOrganismName = value;
          return this;
        }

        /**
         * @return The name of the paternal species constituting the hybrid organism shall be specified.
         */
        public String getPaternalOrganismName() { 
          return this.paternalOrganismName == null ? null : this.paternalOrganismName.getValue();
        }

        /**
         * @param value The name of the paternal species constituting the hybrid organism shall be specified.
         */
        public SubstanceSourceMaterialOrganismHybridComponent setPaternalOrganismName(String value) { 
          if (Utilities.noString(value))
            this.paternalOrganismName = null;
          else {
            if (this.paternalOrganismName == null)
              this.paternalOrganismName = new StringType();
            this.paternalOrganismName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #hybridType} (The hybrid type of an organism shall be specified.)
         */
        public CodeableConcept getHybridType() { 
          if (this.hybridType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismHybridComponent.hybridType");
            else if (Configuration.doAutoCreate())
              this.hybridType = new CodeableConcept(); // cc
          return this.hybridType;
        }

        public boolean hasHybridType() { 
          return this.hybridType != null && !this.hybridType.isEmpty();
        }

        /**
         * @param value {@link #hybridType} (The hybrid type of an organism shall be specified.)
         */
        public SubstanceSourceMaterialOrganismHybridComponent setHybridType(CodeableConcept value) { 
          this.hybridType = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("maternalOrganismId", "string", "The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.", 0, 1, maternalOrganismId));
          children.add(new Property("maternalOrganismName", "string", "The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.", 0, 1, maternalOrganismName));
          children.add(new Property("paternalOrganismId", "string", "The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary.", 0, 1, paternalOrganismId));
          children.add(new Property("paternalOrganismName", "string", "The name of the paternal species constituting the hybrid organism shall be specified.", 0, 1, paternalOrganismName));
          children.add(new Property("hybridType", "CodeableConcept", "The hybrid type of an organism shall be specified.", 0, 1, hybridType));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1179977063: /*maternalOrganismId*/  return new Property("maternalOrganismId", "string", "The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.", 0, 1, maternalOrganismId);
          case -86441847: /*maternalOrganismName*/  return new Property("maternalOrganismName", "string", "The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal and which is paternal.", 0, 1, maternalOrganismName);
          case 123773174: /*paternalOrganismId*/  return new Property("paternalOrganismId", "string", "The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled vocabulary.", 0, 1, paternalOrganismId);
          case -1312914522: /*paternalOrganismName*/  return new Property("paternalOrganismName", "string", "The name of the paternal species constituting the hybrid organism shall be specified.", 0, 1, paternalOrganismName);
          case 1572734806: /*hybridType*/  return new Property("hybridType", "CodeableConcept", "The hybrid type of an organism shall be specified.", 0, 1, hybridType);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1179977063: /*maternalOrganismId*/ return this.maternalOrganismId == null ? new Base[0] : new Base[] {this.maternalOrganismId}; // StringType
        case -86441847: /*maternalOrganismName*/ return this.maternalOrganismName == null ? new Base[0] : new Base[] {this.maternalOrganismName}; // StringType
        case 123773174: /*paternalOrganismId*/ return this.paternalOrganismId == null ? new Base[0] : new Base[] {this.paternalOrganismId}; // StringType
        case -1312914522: /*paternalOrganismName*/ return this.paternalOrganismName == null ? new Base[0] : new Base[] {this.paternalOrganismName}; // StringType
        case 1572734806: /*hybridType*/ return this.hybridType == null ? new Base[0] : new Base[] {this.hybridType}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1179977063: // maternalOrganismId
          this.maternalOrganismId = castToString(value); // StringType
          return value;
        case -86441847: // maternalOrganismName
          this.maternalOrganismName = castToString(value); // StringType
          return value;
        case 123773174: // paternalOrganismId
          this.paternalOrganismId = castToString(value); // StringType
          return value;
        case -1312914522: // paternalOrganismName
          this.paternalOrganismName = castToString(value); // StringType
          return value;
        case 1572734806: // hybridType
          this.hybridType = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("maternalOrganismId")) {
          this.maternalOrganismId = castToString(value); // StringType
        } else if (name.equals("maternalOrganismName")) {
          this.maternalOrganismName = castToString(value); // StringType
        } else if (name.equals("paternalOrganismId")) {
          this.paternalOrganismId = castToString(value); // StringType
        } else if (name.equals("paternalOrganismName")) {
          this.paternalOrganismName = castToString(value); // StringType
        } else if (name.equals("hybridType")) {
          this.hybridType = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1179977063:  return getMaternalOrganismIdElement();
        case -86441847:  return getMaternalOrganismNameElement();
        case 123773174:  return getPaternalOrganismIdElement();
        case -1312914522:  return getPaternalOrganismNameElement();
        case 1572734806:  return getHybridType(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1179977063: /*maternalOrganismId*/ return new String[] {"string"};
        case -86441847: /*maternalOrganismName*/ return new String[] {"string"};
        case 123773174: /*paternalOrganismId*/ return new String[] {"string"};
        case -1312914522: /*paternalOrganismName*/ return new String[] {"string"};
        case 1572734806: /*hybridType*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("maternalOrganismId")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.maternalOrganismId");
        }
        else if (name.equals("maternalOrganismName")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.maternalOrganismName");
        }
        else if (name.equals("paternalOrganismId")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.paternalOrganismId");
        }
        else if (name.equals("paternalOrganismName")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.paternalOrganismName");
        }
        else if (name.equals("hybridType")) {
          this.hybridType = new CodeableConcept();
          return this.hybridType;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSourceMaterialOrganismHybridComponent copy() {
        SubstanceSourceMaterialOrganismHybridComponent dst = new SubstanceSourceMaterialOrganismHybridComponent();
        copyValues(dst);
        dst.maternalOrganismId = maternalOrganismId == null ? null : maternalOrganismId.copy();
        dst.maternalOrganismName = maternalOrganismName == null ? null : maternalOrganismName.copy();
        dst.paternalOrganismId = paternalOrganismId == null ? null : paternalOrganismId.copy();
        dst.paternalOrganismName = paternalOrganismName == null ? null : paternalOrganismName.copy();
        dst.hybridType = hybridType == null ? null : hybridType.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismHybridComponent))
          return false;
        SubstanceSourceMaterialOrganismHybridComponent o = (SubstanceSourceMaterialOrganismHybridComponent) other_;
        return compareDeep(maternalOrganismId, o.maternalOrganismId, true) && compareDeep(maternalOrganismName, o.maternalOrganismName, true)
           && compareDeep(paternalOrganismId, o.paternalOrganismId, true) && compareDeep(paternalOrganismName, o.paternalOrganismName, true)
           && compareDeep(hybridType, o.hybridType, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismHybridComponent))
          return false;
        SubstanceSourceMaterialOrganismHybridComponent o = (SubstanceSourceMaterialOrganismHybridComponent) other_;
        return compareValues(maternalOrganismId, o.maternalOrganismId, true) && compareValues(maternalOrganismName, o.maternalOrganismName, true)
           && compareValues(paternalOrganismId, o.paternalOrganismId, true) && compareValues(paternalOrganismName, o.paternalOrganismName, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(maternalOrganismId, maternalOrganismName
          , paternalOrganismId, paternalOrganismName, hybridType);
      }

  public String fhirType() {
    return "SubstanceSourceMaterial.organism.hybrid";

  }

  }

    @Block()
    public static class SubstanceSourceMaterialOrganismOrganismGeneralComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kingdom of an organism shall be specified.
         */
        @Child(name = "kingdom", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The kingdom of an organism shall be specified", formalDefinition="The kingdom of an organism shall be specified." )
        protected CodeableConcept kingdom;

        /**
         * The phylum of an organism shall be specified.
         */
        @Child(name = "phylum", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The phylum of an organism shall be specified", formalDefinition="The phylum of an organism shall be specified." )
        protected CodeableConcept phylum;

        /**
         * The class of an organism shall be specified.
         */
        @Child(name = "class", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The class of an organism shall be specified", formalDefinition="The class of an organism shall be specified." )
        protected CodeableConcept class_;

        /**
         * The order of an organism shall be specified,.
         */
        @Child(name = "order", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The order of an organism shall be specified,", formalDefinition="The order of an organism shall be specified,." )
        protected CodeableConcept order;

        private static final long serialVersionUID = 659838613L;

    /**
     * Constructor
     */
      public SubstanceSourceMaterialOrganismOrganismGeneralComponent() {
        super();
      }

        /**
         * @return {@link #kingdom} (The kingdom of an organism shall be specified.)
         */
        public CodeableConcept getKingdom() { 
          if (this.kingdom == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismOrganismGeneralComponent.kingdom");
            else if (Configuration.doAutoCreate())
              this.kingdom = new CodeableConcept(); // cc
          return this.kingdom;
        }

        public boolean hasKingdom() { 
          return this.kingdom != null && !this.kingdom.isEmpty();
        }

        /**
         * @param value {@link #kingdom} (The kingdom of an organism shall be specified.)
         */
        public SubstanceSourceMaterialOrganismOrganismGeneralComponent setKingdom(CodeableConcept value) { 
          this.kingdom = value;
          return this;
        }

        /**
         * @return {@link #phylum} (The phylum of an organism shall be specified.)
         */
        public CodeableConcept getPhylum() { 
          if (this.phylum == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismOrganismGeneralComponent.phylum");
            else if (Configuration.doAutoCreate())
              this.phylum = new CodeableConcept(); // cc
          return this.phylum;
        }

        public boolean hasPhylum() { 
          return this.phylum != null && !this.phylum.isEmpty();
        }

        /**
         * @param value {@link #phylum} (The phylum of an organism shall be specified.)
         */
        public SubstanceSourceMaterialOrganismOrganismGeneralComponent setPhylum(CodeableConcept value) { 
          this.phylum = value;
          return this;
        }

        /**
         * @return {@link #class_} (The class of an organism shall be specified.)
         */
        public CodeableConcept getClass_() { 
          if (this.class_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismOrganismGeneralComponent.class_");
            else if (Configuration.doAutoCreate())
              this.class_ = new CodeableConcept(); // cc
          return this.class_;
        }

        public boolean hasClass_() { 
          return this.class_ != null && !this.class_.isEmpty();
        }

        /**
         * @param value {@link #class_} (The class of an organism shall be specified.)
         */
        public SubstanceSourceMaterialOrganismOrganismGeneralComponent setClass_(CodeableConcept value) { 
          this.class_ = value;
          return this;
        }

        /**
         * @return {@link #order} (The order of an organism shall be specified,.)
         */
        public CodeableConcept getOrder() { 
          if (this.order == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialOrganismOrganismGeneralComponent.order");
            else if (Configuration.doAutoCreate())
              this.order = new CodeableConcept(); // cc
          return this.order;
        }

        public boolean hasOrder() { 
          return this.order != null && !this.order.isEmpty();
        }

        /**
         * @param value {@link #order} (The order of an organism shall be specified,.)
         */
        public SubstanceSourceMaterialOrganismOrganismGeneralComponent setOrder(CodeableConcept value) { 
          this.order = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("kingdom", "CodeableConcept", "The kingdom of an organism shall be specified.", 0, 1, kingdom));
          children.add(new Property("phylum", "CodeableConcept", "The phylum of an organism shall be specified.", 0, 1, phylum));
          children.add(new Property("class", "CodeableConcept", "The class of an organism shall be specified.", 0, 1, class_));
          children.add(new Property("order", "CodeableConcept", "The order of an organism shall be specified,.", 0, 1, order));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -710537653: /*kingdom*/  return new Property("kingdom", "CodeableConcept", "The kingdom of an organism shall be specified.", 0, 1, kingdom);
          case -988743965: /*phylum*/  return new Property("phylum", "CodeableConcept", "The phylum of an organism shall be specified.", 0, 1, phylum);
          case 94742904: /*class*/  return new Property("class", "CodeableConcept", "The class of an organism shall be specified.", 0, 1, class_);
          case 106006350: /*order*/  return new Property("order", "CodeableConcept", "The order of an organism shall be specified,.", 0, 1, order);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -710537653: /*kingdom*/ return this.kingdom == null ? new Base[0] : new Base[] {this.kingdom}; // CodeableConcept
        case -988743965: /*phylum*/ return this.phylum == null ? new Base[0] : new Base[] {this.phylum}; // CodeableConcept
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : new Base[] {this.class_}; // CodeableConcept
        case 106006350: /*order*/ return this.order == null ? new Base[0] : new Base[] {this.order}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -710537653: // kingdom
          this.kingdom = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -988743965: // phylum
          this.phylum = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 94742904: // class
          this.class_ = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 106006350: // order
          this.order = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("kingdom")) {
          this.kingdom = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("phylum")) {
          this.phylum = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("class")) {
          this.class_ = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("order")) {
          this.order = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -710537653:  return getKingdom(); 
        case -988743965:  return getPhylum(); 
        case 94742904:  return getClass_(); 
        case 106006350:  return getOrder(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -710537653: /*kingdom*/ return new String[] {"CodeableConcept"};
        case -988743965: /*phylum*/ return new String[] {"CodeableConcept"};
        case 94742904: /*class*/ return new String[] {"CodeableConcept"};
        case 106006350: /*order*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("kingdom")) {
          this.kingdom = new CodeableConcept();
          return this.kingdom;
        }
        else if (name.equals("phylum")) {
          this.phylum = new CodeableConcept();
          return this.phylum;
        }
        else if (name.equals("class")) {
          this.class_ = new CodeableConcept();
          return this.class_;
        }
        else if (name.equals("order")) {
          this.order = new CodeableConcept();
          return this.order;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSourceMaterialOrganismOrganismGeneralComponent copy() {
        SubstanceSourceMaterialOrganismOrganismGeneralComponent dst = new SubstanceSourceMaterialOrganismOrganismGeneralComponent();
        copyValues(dst);
        dst.kingdom = kingdom == null ? null : kingdom.copy();
        dst.phylum = phylum == null ? null : phylum.copy();
        dst.class_ = class_ == null ? null : class_.copy();
        dst.order = order == null ? null : order.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismOrganismGeneralComponent))
          return false;
        SubstanceSourceMaterialOrganismOrganismGeneralComponent o = (SubstanceSourceMaterialOrganismOrganismGeneralComponent) other_;
        return compareDeep(kingdom, o.kingdom, true) && compareDeep(phylum, o.phylum, true) && compareDeep(class_, o.class_, true)
           && compareDeep(order, o.order, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialOrganismOrganismGeneralComponent))
          return false;
        SubstanceSourceMaterialOrganismOrganismGeneralComponent o = (SubstanceSourceMaterialOrganismOrganismGeneralComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(kingdom, phylum, class_
          , order);
      }

  public String fhirType() {
    return "SubstanceSourceMaterial.organism.organismGeneral";

  }

  }

    @Block()
    public static class SubstanceSourceMaterialPartDescriptionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Entity of anatomical origin of source material within an organism.
         */
        @Child(name = "part", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Entity of anatomical origin of source material within an organism", formalDefinition="Entity of anatomical origin of source material within an organism." )
        protected CodeableConcept part;

        /**
         * The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. Multiple alternative locations may apply.
         */
        @Child(name = "partLocation", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. Multiple alternative locations may apply", formalDefinition="The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. Multiple alternative locations may apply." )
        protected CodeableConcept partLocation;

        private static final long serialVersionUID = 308875915L;

    /**
     * Constructor
     */
      public SubstanceSourceMaterialPartDescriptionComponent() {
        super();
      }

        /**
         * @return {@link #part} (Entity of anatomical origin of source material within an organism.)
         */
        public CodeableConcept getPart() { 
          if (this.part == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialPartDescriptionComponent.part");
            else if (Configuration.doAutoCreate())
              this.part = new CodeableConcept(); // cc
          return this.part;
        }

        public boolean hasPart() { 
          return this.part != null && !this.part.isEmpty();
        }

        /**
         * @param value {@link #part} (Entity of anatomical origin of source material within an organism.)
         */
        public SubstanceSourceMaterialPartDescriptionComponent setPart(CodeableConcept value) { 
          this.part = value;
          return this;
        }

        /**
         * @return {@link #partLocation} (The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. Multiple alternative locations may apply.)
         */
        public CodeableConcept getPartLocation() { 
          if (this.partLocation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSourceMaterialPartDescriptionComponent.partLocation");
            else if (Configuration.doAutoCreate())
              this.partLocation = new CodeableConcept(); // cc
          return this.partLocation;
        }

        public boolean hasPartLocation() { 
          return this.partLocation != null && !this.partLocation.isEmpty();
        }

        /**
         * @param value {@link #partLocation} (The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. Multiple alternative locations may apply.)
         */
        public SubstanceSourceMaterialPartDescriptionComponent setPartLocation(CodeableConcept value) { 
          this.partLocation = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("part", "CodeableConcept", "Entity of anatomical origin of source material within an organism.", 0, 1, part));
          children.add(new Property("partLocation", "CodeableConcept", "The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. Multiple alternative locations may apply.", 0, 1, partLocation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3433459: /*part*/  return new Property("part", "CodeableConcept", "Entity of anatomical origin of source material within an organism.", 0, 1, part);
          case 893437128: /*partLocation*/  return new Property("partLocation", "CodeableConcept", "The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. Multiple alternative locations may apply.", 0, 1, partLocation);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433459: /*part*/ return this.part == null ? new Base[0] : new Base[] {this.part}; // CodeableConcept
        case 893437128: /*partLocation*/ return this.partLocation == null ? new Base[0] : new Base[] {this.partLocation}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433459: // part
          this.part = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 893437128: // partLocation
          this.partLocation = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("part")) {
          this.part = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("partLocation")) {
          this.partLocation = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433459:  return getPart(); 
        case 893437128:  return getPartLocation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433459: /*part*/ return new String[] {"CodeableConcept"};
        case 893437128: /*partLocation*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("part")) {
          this.part = new CodeableConcept();
          return this.part;
        }
        else if (name.equals("partLocation")) {
          this.partLocation = new CodeableConcept();
          return this.partLocation;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSourceMaterialPartDescriptionComponent copy() {
        SubstanceSourceMaterialPartDescriptionComponent dst = new SubstanceSourceMaterialPartDescriptionComponent();
        copyValues(dst);
        dst.part = part == null ? null : part.copy();
        dst.partLocation = partLocation == null ? null : partLocation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialPartDescriptionComponent))
          return false;
        SubstanceSourceMaterialPartDescriptionComponent o = (SubstanceSourceMaterialPartDescriptionComponent) other_;
        return compareDeep(part, o.part, true) && compareDeep(partLocation, o.partLocation, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterialPartDescriptionComponent))
          return false;
        SubstanceSourceMaterialPartDescriptionComponent o = (SubstanceSourceMaterialPartDescriptionComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(part, partLocation);
      }

  public String fhirType() {
    return "SubstanceSourceMaterial.partDescription";

  }

  }

    /**
     * General high level classification of the source material specific to the origin of the material.
     */
    @Child(name = "sourceMaterialClass", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="General high level classification of the source material specific to the origin of the material", formalDefinition="General high level classification of the source material specific to the origin of the material." )
    protected CodeableConcept sourceMaterialClass;

    /**
     * The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause refers to the class of infectious agent.
     */
    @Child(name = "sourceMaterialType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause refers to the class of infectious agent", formalDefinition="The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause refers to the class of infectious agent." )
    protected CodeableConcept sourceMaterialType;

    /**
     * The state of the source material when extracted.
     */
    @Child(name = "sourceMaterialState", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The state of the source material when extracted", formalDefinition="The state of the source material when extracted." )
    protected CodeableConcept sourceMaterialState;

    /**
     * The unique identifier associated with the source material parent organism shall be specified.
     */
    @Child(name = "organismId", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The unique identifier associated with the source material parent organism shall be specified", formalDefinition="The unique identifier associated with the source material parent organism shall be specified." )
    protected Identifier organismId;

    /**
     * The organism accepted Scientific name shall be provided based on the organism taxonomy.
     */
    @Child(name = "organismName", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The organism accepted Scientific name shall be provided based on the organism taxonomy", formalDefinition="The organism accepted Scientific name shall be provided based on the organism taxonomy." )
    protected StringType organismName;

    /**
     * The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or Ginkgo biloba L. (Whole plant).
     */
    @Child(name = "parentSubstanceId", type = {Identifier.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or Ginkgo biloba L. (Whole plant)", formalDefinition="The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or Ginkgo biloba L. (Whole plant)." )
    protected List<Identifier> parentSubstanceId;

    /**
     * The parent substance of the Herbal Drug, or Herbal preparation.
     */
    @Child(name = "parentSubstanceName", type = {StringType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The parent substance of the Herbal Drug, or Herbal preparation", formalDefinition="The parent substance of the Herbal Drug, or Herbal preparation." )
    protected List<StringType> parentSubstanceName;

    /**
     * The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.
     */
    @Child(name = "countryOfOrigin", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate", formalDefinition="The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate." )
    protected List<CodeableConcept> countryOfOrigin;

    /**
     * The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.
     */
    @Child(name = "geographicalLocation", type = {StringType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The place/region where the plant is harvested or the places/regions where the animal source material has its habitat", formalDefinition="The place/region where the plant is harvested or the places/regions where the animal source material has its habitat." )
    protected List<StringType> geographicalLocation;

    /**
     * Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the substance is significantly different in these stages (e.g. foetal bovine serum).
     */
    @Child(name = "developmentStage", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the substance is significantly different in these stages (e.g. foetal bovine serum)", formalDefinition="Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the substance is significantly different in these stages (e.g. foetal bovine serum)." )
    protected CodeableConcept developmentStage;

    /**
     * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance Group 1 levels.
     */
    @Child(name = "fractionDescription", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance Group 1 levels", formalDefinition="Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance Group 1 levels." )
    protected List<SubstanceSourceMaterialFractionDescriptionComponent> fractionDescription;

    /**
     * This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., Leaf.
     */
    @Child(name = "organism", type = {}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., Leaf", formalDefinition="This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., Leaf." )
    protected SubstanceSourceMaterialOrganismComponent organism;

    /**
     * To do.
     */
    @Child(name = "partDescription", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="To do", formalDefinition="To do." )
    protected List<SubstanceSourceMaterialPartDescriptionComponent> partDescription;

    private static final long serialVersionUID = 442657667L;

  /**
   * Constructor
   */
    public SubstanceSourceMaterial() {
      super();
    }

    /**
     * @return {@link #sourceMaterialClass} (General high level classification of the source material specific to the origin of the material.)
     */
    public CodeableConcept getSourceMaterialClass() { 
      if (this.sourceMaterialClass == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSourceMaterial.sourceMaterialClass");
        else if (Configuration.doAutoCreate())
          this.sourceMaterialClass = new CodeableConcept(); // cc
      return this.sourceMaterialClass;
    }

    public boolean hasSourceMaterialClass() { 
      return this.sourceMaterialClass != null && !this.sourceMaterialClass.isEmpty();
    }

    /**
     * @param value {@link #sourceMaterialClass} (General high level classification of the source material specific to the origin of the material.)
     */
    public SubstanceSourceMaterial setSourceMaterialClass(CodeableConcept value) { 
      this.sourceMaterialClass = value;
      return this;
    }

    /**
     * @return {@link #sourceMaterialType} (The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause refers to the class of infectious agent.)
     */
    public CodeableConcept getSourceMaterialType() { 
      if (this.sourceMaterialType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSourceMaterial.sourceMaterialType");
        else if (Configuration.doAutoCreate())
          this.sourceMaterialType = new CodeableConcept(); // cc
      return this.sourceMaterialType;
    }

    public boolean hasSourceMaterialType() { 
      return this.sourceMaterialType != null && !this.sourceMaterialType.isEmpty();
    }

    /**
     * @param value {@link #sourceMaterialType} (The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause refers to the class of infectious agent.)
     */
    public SubstanceSourceMaterial setSourceMaterialType(CodeableConcept value) { 
      this.sourceMaterialType = value;
      return this;
    }

    /**
     * @return {@link #sourceMaterialState} (The state of the source material when extracted.)
     */
    public CodeableConcept getSourceMaterialState() { 
      if (this.sourceMaterialState == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSourceMaterial.sourceMaterialState");
        else if (Configuration.doAutoCreate())
          this.sourceMaterialState = new CodeableConcept(); // cc
      return this.sourceMaterialState;
    }

    public boolean hasSourceMaterialState() { 
      return this.sourceMaterialState != null && !this.sourceMaterialState.isEmpty();
    }

    /**
     * @param value {@link #sourceMaterialState} (The state of the source material when extracted.)
     */
    public SubstanceSourceMaterial setSourceMaterialState(CodeableConcept value) { 
      this.sourceMaterialState = value;
      return this;
    }

    /**
     * @return {@link #organismId} (The unique identifier associated with the source material parent organism shall be specified.)
     */
    public Identifier getOrganismId() { 
      if (this.organismId == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSourceMaterial.organismId");
        else if (Configuration.doAutoCreate())
          this.organismId = new Identifier(); // cc
      return this.organismId;
    }

    public boolean hasOrganismId() { 
      return this.organismId != null && !this.organismId.isEmpty();
    }

    /**
     * @param value {@link #organismId} (The unique identifier associated with the source material parent organism shall be specified.)
     */
    public SubstanceSourceMaterial setOrganismId(Identifier value) { 
      this.organismId = value;
      return this;
    }

    /**
     * @return {@link #organismName} (The organism accepted Scientific name shall be provided based on the organism taxonomy.). This is the underlying object with id, value and extensions. The accessor "getOrganismName" gives direct access to the value
     */
    public StringType getOrganismNameElement() { 
      if (this.organismName == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSourceMaterial.organismName");
        else if (Configuration.doAutoCreate())
          this.organismName = new StringType(); // bb
      return this.organismName;
    }

    public boolean hasOrganismNameElement() { 
      return this.organismName != null && !this.organismName.isEmpty();
    }

    public boolean hasOrganismName() { 
      return this.organismName != null && !this.organismName.isEmpty();
    }

    /**
     * @param value {@link #organismName} (The organism accepted Scientific name shall be provided based on the organism taxonomy.). This is the underlying object with id, value and extensions. The accessor "getOrganismName" gives direct access to the value
     */
    public SubstanceSourceMaterial setOrganismNameElement(StringType value) { 
      this.organismName = value;
      return this;
    }

    /**
     * @return The organism accepted Scientific name shall be provided based on the organism taxonomy.
     */
    public String getOrganismName() { 
      return this.organismName == null ? null : this.organismName.getValue();
    }

    /**
     * @param value The organism accepted Scientific name shall be provided based on the organism taxonomy.
     */
    public SubstanceSourceMaterial setOrganismName(String value) { 
      if (Utilities.noString(value))
        this.organismName = null;
      else {
        if (this.organismName == null)
          this.organismName = new StringType();
        this.organismName.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #parentSubstanceId} (The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or Ginkgo biloba L. (Whole plant).)
     */
    public List<Identifier> getParentSubstanceId() { 
      if (this.parentSubstanceId == null)
        this.parentSubstanceId = new ArrayList<Identifier>();
      return this.parentSubstanceId;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSourceMaterial setParentSubstanceId(List<Identifier> theParentSubstanceId) { 
      this.parentSubstanceId = theParentSubstanceId;
      return this;
    }

    public boolean hasParentSubstanceId() { 
      if (this.parentSubstanceId == null)
        return false;
      for (Identifier item : this.parentSubstanceId)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addParentSubstanceId() { //3
      Identifier t = new Identifier();
      if (this.parentSubstanceId == null)
        this.parentSubstanceId = new ArrayList<Identifier>();
      this.parentSubstanceId.add(t);
      return t;
    }

    public SubstanceSourceMaterial addParentSubstanceId(Identifier t) { //3
      if (t == null)
        return this;
      if (this.parentSubstanceId == null)
        this.parentSubstanceId = new ArrayList<Identifier>();
      this.parentSubstanceId.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #parentSubstanceId}, creating it if it does not already exist
     */
    public Identifier getParentSubstanceIdFirstRep() { 
      if (getParentSubstanceId().isEmpty()) {
        addParentSubstanceId();
      }
      return getParentSubstanceId().get(0);
    }

    /**
     * @return {@link #parentSubstanceName} (The parent substance of the Herbal Drug, or Herbal preparation.)
     */
    public List<StringType> getParentSubstanceName() { 
      if (this.parentSubstanceName == null)
        this.parentSubstanceName = new ArrayList<StringType>();
      return this.parentSubstanceName;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSourceMaterial setParentSubstanceName(List<StringType> theParentSubstanceName) { 
      this.parentSubstanceName = theParentSubstanceName;
      return this;
    }

    public boolean hasParentSubstanceName() { 
      if (this.parentSubstanceName == null)
        return false;
      for (StringType item : this.parentSubstanceName)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parentSubstanceName} (The parent substance of the Herbal Drug, or Herbal preparation.)
     */
    public StringType addParentSubstanceNameElement() {//2 
      StringType t = new StringType();
      if (this.parentSubstanceName == null)
        this.parentSubstanceName = new ArrayList<StringType>();
      this.parentSubstanceName.add(t);
      return t;
    }

    /**
     * @param value {@link #parentSubstanceName} (The parent substance of the Herbal Drug, or Herbal preparation.)
     */
    public SubstanceSourceMaterial addParentSubstanceName(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.parentSubstanceName == null)
        this.parentSubstanceName = new ArrayList<StringType>();
      this.parentSubstanceName.add(t);
      return this;
    }

    /**
     * @param value {@link #parentSubstanceName} (The parent substance of the Herbal Drug, or Herbal preparation.)
     */
    public boolean hasParentSubstanceName(String value) { 
      if (this.parentSubstanceName == null)
        return false;
      for (StringType v : this.parentSubstanceName)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #countryOfOrigin} (The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.)
     */
    public List<CodeableConcept> getCountryOfOrigin() { 
      if (this.countryOfOrigin == null)
        this.countryOfOrigin = new ArrayList<CodeableConcept>();
      return this.countryOfOrigin;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSourceMaterial setCountryOfOrigin(List<CodeableConcept> theCountryOfOrigin) { 
      this.countryOfOrigin = theCountryOfOrigin;
      return this;
    }

    public boolean hasCountryOfOrigin() { 
      if (this.countryOfOrigin == null)
        return false;
      for (CodeableConcept item : this.countryOfOrigin)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCountryOfOrigin() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.countryOfOrigin == null)
        this.countryOfOrigin = new ArrayList<CodeableConcept>();
      this.countryOfOrigin.add(t);
      return t;
    }

    public SubstanceSourceMaterial addCountryOfOrigin(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.countryOfOrigin == null)
        this.countryOfOrigin = new ArrayList<CodeableConcept>();
      this.countryOfOrigin.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #countryOfOrigin}, creating it if it does not already exist
     */
    public CodeableConcept getCountryOfOriginFirstRep() { 
      if (getCountryOfOrigin().isEmpty()) {
        addCountryOfOrigin();
      }
      return getCountryOfOrigin().get(0);
    }

    /**
     * @return {@link #geographicalLocation} (The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.)
     */
    public List<StringType> getGeographicalLocation() { 
      if (this.geographicalLocation == null)
        this.geographicalLocation = new ArrayList<StringType>();
      return this.geographicalLocation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSourceMaterial setGeographicalLocation(List<StringType> theGeographicalLocation) { 
      this.geographicalLocation = theGeographicalLocation;
      return this;
    }

    public boolean hasGeographicalLocation() { 
      if (this.geographicalLocation == null)
        return false;
      for (StringType item : this.geographicalLocation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #geographicalLocation} (The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.)
     */
    public StringType addGeographicalLocationElement() {//2 
      StringType t = new StringType();
      if (this.geographicalLocation == null)
        this.geographicalLocation = new ArrayList<StringType>();
      this.geographicalLocation.add(t);
      return t;
    }

    /**
     * @param value {@link #geographicalLocation} (The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.)
     */
    public SubstanceSourceMaterial addGeographicalLocation(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.geographicalLocation == null)
        this.geographicalLocation = new ArrayList<StringType>();
      this.geographicalLocation.add(t);
      return this;
    }

    /**
     * @param value {@link #geographicalLocation} (The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.)
     */
    public boolean hasGeographicalLocation(String value) { 
      if (this.geographicalLocation == null)
        return false;
      for (StringType v : this.geographicalLocation)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #developmentStage} (Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the substance is significantly different in these stages (e.g. foetal bovine serum).)
     */
    public CodeableConcept getDevelopmentStage() { 
      if (this.developmentStage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSourceMaterial.developmentStage");
        else if (Configuration.doAutoCreate())
          this.developmentStage = new CodeableConcept(); // cc
      return this.developmentStage;
    }

    public boolean hasDevelopmentStage() { 
      return this.developmentStage != null && !this.developmentStage.isEmpty();
    }

    /**
     * @param value {@link #developmentStage} (Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the substance is significantly different in these stages (e.g. foetal bovine serum).)
     */
    public SubstanceSourceMaterial setDevelopmentStage(CodeableConcept value) { 
      this.developmentStage = value;
      return this;
    }

    /**
     * @return {@link #fractionDescription} (Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance Group 1 levels.)
     */
    public List<SubstanceSourceMaterialFractionDescriptionComponent> getFractionDescription() { 
      if (this.fractionDescription == null)
        this.fractionDescription = new ArrayList<SubstanceSourceMaterialFractionDescriptionComponent>();
      return this.fractionDescription;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSourceMaterial setFractionDescription(List<SubstanceSourceMaterialFractionDescriptionComponent> theFractionDescription) { 
      this.fractionDescription = theFractionDescription;
      return this;
    }

    public boolean hasFractionDescription() { 
      if (this.fractionDescription == null)
        return false;
      for (SubstanceSourceMaterialFractionDescriptionComponent item : this.fractionDescription)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSourceMaterialFractionDescriptionComponent addFractionDescription() { //3
      SubstanceSourceMaterialFractionDescriptionComponent t = new SubstanceSourceMaterialFractionDescriptionComponent();
      if (this.fractionDescription == null)
        this.fractionDescription = new ArrayList<SubstanceSourceMaterialFractionDescriptionComponent>();
      this.fractionDescription.add(t);
      return t;
    }

    public SubstanceSourceMaterial addFractionDescription(SubstanceSourceMaterialFractionDescriptionComponent t) { //3
      if (t == null)
        return this;
      if (this.fractionDescription == null)
        this.fractionDescription = new ArrayList<SubstanceSourceMaterialFractionDescriptionComponent>();
      this.fractionDescription.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #fractionDescription}, creating it if it does not already exist
     */
    public SubstanceSourceMaterialFractionDescriptionComponent getFractionDescriptionFirstRep() { 
      if (getFractionDescription().isEmpty()) {
        addFractionDescription();
      }
      return getFractionDescription().get(0);
    }

    /**
     * @return {@link #organism} (This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., Leaf.)
     */
    public SubstanceSourceMaterialOrganismComponent getOrganism() { 
      if (this.organism == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSourceMaterial.organism");
        else if (Configuration.doAutoCreate())
          this.organism = new SubstanceSourceMaterialOrganismComponent(); // cc
      return this.organism;
    }

    public boolean hasOrganism() { 
      return this.organism != null && !this.organism.isEmpty();
    }

    /**
     * @param value {@link #organism} (This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., Leaf.)
     */
    public SubstanceSourceMaterial setOrganism(SubstanceSourceMaterialOrganismComponent value) { 
      this.organism = value;
      return this;
    }

    /**
     * @return {@link #partDescription} (To do.)
     */
    public List<SubstanceSourceMaterialPartDescriptionComponent> getPartDescription() { 
      if (this.partDescription == null)
        this.partDescription = new ArrayList<SubstanceSourceMaterialPartDescriptionComponent>();
      return this.partDescription;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSourceMaterial setPartDescription(List<SubstanceSourceMaterialPartDescriptionComponent> thePartDescription) { 
      this.partDescription = thePartDescription;
      return this;
    }

    public boolean hasPartDescription() { 
      if (this.partDescription == null)
        return false;
      for (SubstanceSourceMaterialPartDescriptionComponent item : this.partDescription)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSourceMaterialPartDescriptionComponent addPartDescription() { //3
      SubstanceSourceMaterialPartDescriptionComponent t = new SubstanceSourceMaterialPartDescriptionComponent();
      if (this.partDescription == null)
        this.partDescription = new ArrayList<SubstanceSourceMaterialPartDescriptionComponent>();
      this.partDescription.add(t);
      return t;
    }

    public SubstanceSourceMaterial addPartDescription(SubstanceSourceMaterialPartDescriptionComponent t) { //3
      if (t == null)
        return this;
      if (this.partDescription == null)
        this.partDescription = new ArrayList<SubstanceSourceMaterialPartDescriptionComponent>();
      this.partDescription.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #partDescription}, creating it if it does not already exist
     */
    public SubstanceSourceMaterialPartDescriptionComponent getPartDescriptionFirstRep() { 
      if (getPartDescription().isEmpty()) {
        addPartDescription();
      }
      return getPartDescription().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("sourceMaterialClass", "CodeableConcept", "General high level classification of the source material specific to the origin of the material.", 0, 1, sourceMaterialClass));
        children.add(new Property("sourceMaterialType", "CodeableConcept", "The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause refers to the class of infectious agent.", 0, 1, sourceMaterialType));
        children.add(new Property("sourceMaterialState", "CodeableConcept", "The state of the source material when extracted.", 0, 1, sourceMaterialState));
        children.add(new Property("organismId", "Identifier", "The unique identifier associated with the source material parent organism shall be specified.", 0, 1, organismId));
        children.add(new Property("organismName", "string", "The organism accepted Scientific name shall be provided based on the organism taxonomy.", 0, 1, organismName));
        children.add(new Property("parentSubstanceId", "Identifier", "The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or Ginkgo biloba L. (Whole plant).", 0, java.lang.Integer.MAX_VALUE, parentSubstanceId));
        children.add(new Property("parentSubstanceName", "string", "The parent substance of the Herbal Drug, or Herbal preparation.", 0, java.lang.Integer.MAX_VALUE, parentSubstanceName));
        children.add(new Property("countryOfOrigin", "CodeableConcept", "The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.", 0, java.lang.Integer.MAX_VALUE, countryOfOrigin));
        children.add(new Property("geographicalLocation", "string", "The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.", 0, java.lang.Integer.MAX_VALUE, geographicalLocation));
        children.add(new Property("developmentStage", "CodeableConcept", "Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the substance is significantly different in these stages (e.g. foetal bovine serum).", 0, 1, developmentStage));
        children.add(new Property("fractionDescription", "", "Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance Group 1 levels.", 0, java.lang.Integer.MAX_VALUE, fractionDescription));
        children.add(new Property("organism", "", "This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., Leaf.", 0, 1, organism));
        children.add(new Property("partDescription", "", "To do.", 0, java.lang.Integer.MAX_VALUE, partDescription));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1253081034: /*sourceMaterialClass*/  return new Property("sourceMaterialClass", "CodeableConcept", "General high level classification of the source material specific to the origin of the material.", 0, 1, sourceMaterialClass);
        case 1622665404: /*sourceMaterialType*/  return new Property("sourceMaterialType", "CodeableConcept", "The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause refers to the class of infectious agent.", 0, 1, sourceMaterialType);
        case -1238066353: /*sourceMaterialState*/  return new Property("sourceMaterialState", "CodeableConcept", "The state of the source material when extracted.", 0, 1, sourceMaterialState);
        case -1965449843: /*organismId*/  return new Property("organismId", "Identifier", "The unique identifier associated with the source material parent organism shall be specified.", 0, 1, organismId);
        case 988460669: /*organismName*/  return new Property("organismName", "string", "The organism accepted Scientific name shall be provided based on the organism taxonomy.", 0, 1, organismName);
        case -675437663: /*parentSubstanceId*/  return new Property("parentSubstanceId", "Identifier", "The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or Ginkgo biloba L. (Whole plant).", 0, java.lang.Integer.MAX_VALUE, parentSubstanceId);
        case -555382895: /*parentSubstanceName*/  return new Property("parentSubstanceName", "string", "The parent substance of the Herbal Drug, or Herbal preparation.", 0, java.lang.Integer.MAX_VALUE, parentSubstanceName);
        case 57176467: /*countryOfOrigin*/  return new Property("countryOfOrigin", "CodeableConcept", "The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.", 0, java.lang.Integer.MAX_VALUE, countryOfOrigin);
        case -1988836681: /*geographicalLocation*/  return new Property("geographicalLocation", "string", "The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.", 0, java.lang.Integer.MAX_VALUE, geographicalLocation);
        case 391529091: /*developmentStage*/  return new Property("developmentStage", "CodeableConcept", "Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the substance is significantly different in these stages (e.g. foetal bovine serum).", 0, 1, developmentStage);
        case 1472689306: /*fractionDescription*/  return new Property("fractionDescription", "", "Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance Group 1 levels.", 0, java.lang.Integer.MAX_VALUE, fractionDescription);
        case 1316389074: /*organism*/  return new Property("organism", "", "This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., Leaf.", 0, 1, organism);
        case -1803623927: /*partDescription*/  return new Property("partDescription", "", "To do.", 0, java.lang.Integer.MAX_VALUE, partDescription);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1253081034: /*sourceMaterialClass*/ return this.sourceMaterialClass == null ? new Base[0] : new Base[] {this.sourceMaterialClass}; // CodeableConcept
        case 1622665404: /*sourceMaterialType*/ return this.sourceMaterialType == null ? new Base[0] : new Base[] {this.sourceMaterialType}; // CodeableConcept
        case -1238066353: /*sourceMaterialState*/ return this.sourceMaterialState == null ? new Base[0] : new Base[] {this.sourceMaterialState}; // CodeableConcept
        case -1965449843: /*organismId*/ return this.organismId == null ? new Base[0] : new Base[] {this.organismId}; // Identifier
        case 988460669: /*organismName*/ return this.organismName == null ? new Base[0] : new Base[] {this.organismName}; // StringType
        case -675437663: /*parentSubstanceId*/ return this.parentSubstanceId == null ? new Base[0] : this.parentSubstanceId.toArray(new Base[this.parentSubstanceId.size()]); // Identifier
        case -555382895: /*parentSubstanceName*/ return this.parentSubstanceName == null ? new Base[0] : this.parentSubstanceName.toArray(new Base[this.parentSubstanceName.size()]); // StringType
        case 57176467: /*countryOfOrigin*/ return this.countryOfOrigin == null ? new Base[0] : this.countryOfOrigin.toArray(new Base[this.countryOfOrigin.size()]); // CodeableConcept
        case -1988836681: /*geographicalLocation*/ return this.geographicalLocation == null ? new Base[0] : this.geographicalLocation.toArray(new Base[this.geographicalLocation.size()]); // StringType
        case 391529091: /*developmentStage*/ return this.developmentStage == null ? new Base[0] : new Base[] {this.developmentStage}; // CodeableConcept
        case 1472689306: /*fractionDescription*/ return this.fractionDescription == null ? new Base[0] : this.fractionDescription.toArray(new Base[this.fractionDescription.size()]); // SubstanceSourceMaterialFractionDescriptionComponent
        case 1316389074: /*organism*/ return this.organism == null ? new Base[0] : new Base[] {this.organism}; // SubstanceSourceMaterialOrganismComponent
        case -1803623927: /*partDescription*/ return this.partDescription == null ? new Base[0] : this.partDescription.toArray(new Base[this.partDescription.size()]); // SubstanceSourceMaterialPartDescriptionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1253081034: // sourceMaterialClass
          this.sourceMaterialClass = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1622665404: // sourceMaterialType
          this.sourceMaterialType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1238066353: // sourceMaterialState
          this.sourceMaterialState = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1965449843: // organismId
          this.organismId = castToIdentifier(value); // Identifier
          return value;
        case 988460669: // organismName
          this.organismName = castToString(value); // StringType
          return value;
        case -675437663: // parentSubstanceId
          this.getParentSubstanceId().add(castToIdentifier(value)); // Identifier
          return value;
        case -555382895: // parentSubstanceName
          this.getParentSubstanceName().add(castToString(value)); // StringType
          return value;
        case 57176467: // countryOfOrigin
          this.getCountryOfOrigin().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1988836681: // geographicalLocation
          this.getGeographicalLocation().add(castToString(value)); // StringType
          return value;
        case 391529091: // developmentStage
          this.developmentStage = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1472689306: // fractionDescription
          this.getFractionDescription().add((SubstanceSourceMaterialFractionDescriptionComponent) value); // SubstanceSourceMaterialFractionDescriptionComponent
          return value;
        case 1316389074: // organism
          this.organism = (SubstanceSourceMaterialOrganismComponent) value; // SubstanceSourceMaterialOrganismComponent
          return value;
        case -1803623927: // partDescription
          this.getPartDescription().add((SubstanceSourceMaterialPartDescriptionComponent) value); // SubstanceSourceMaterialPartDescriptionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sourceMaterialClass")) {
          this.sourceMaterialClass = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("sourceMaterialType")) {
          this.sourceMaterialType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("sourceMaterialState")) {
          this.sourceMaterialState = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("organismId")) {
          this.organismId = castToIdentifier(value); // Identifier
        } else if (name.equals("organismName")) {
          this.organismName = castToString(value); // StringType
        } else if (name.equals("parentSubstanceId")) {
          this.getParentSubstanceId().add(castToIdentifier(value));
        } else if (name.equals("parentSubstanceName")) {
          this.getParentSubstanceName().add(castToString(value));
        } else if (name.equals("countryOfOrigin")) {
          this.getCountryOfOrigin().add(castToCodeableConcept(value));
        } else if (name.equals("geographicalLocation")) {
          this.getGeographicalLocation().add(castToString(value));
        } else if (name.equals("developmentStage")) {
          this.developmentStage = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("fractionDescription")) {
          this.getFractionDescription().add((SubstanceSourceMaterialFractionDescriptionComponent) value);
        } else if (name.equals("organism")) {
          this.organism = (SubstanceSourceMaterialOrganismComponent) value; // SubstanceSourceMaterialOrganismComponent
        } else if (name.equals("partDescription")) {
          this.getPartDescription().add((SubstanceSourceMaterialPartDescriptionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1253081034:  return getSourceMaterialClass(); 
        case 1622665404:  return getSourceMaterialType(); 
        case -1238066353:  return getSourceMaterialState(); 
        case -1965449843:  return getOrganismId(); 
        case 988460669:  return getOrganismNameElement();
        case -675437663:  return addParentSubstanceId(); 
        case -555382895:  return addParentSubstanceNameElement();
        case 57176467:  return addCountryOfOrigin(); 
        case -1988836681:  return addGeographicalLocationElement();
        case 391529091:  return getDevelopmentStage(); 
        case 1472689306:  return addFractionDescription(); 
        case 1316389074:  return getOrganism(); 
        case -1803623927:  return addPartDescription(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1253081034: /*sourceMaterialClass*/ return new String[] {"CodeableConcept"};
        case 1622665404: /*sourceMaterialType*/ return new String[] {"CodeableConcept"};
        case -1238066353: /*sourceMaterialState*/ return new String[] {"CodeableConcept"};
        case -1965449843: /*organismId*/ return new String[] {"Identifier"};
        case 988460669: /*organismName*/ return new String[] {"string"};
        case -675437663: /*parentSubstanceId*/ return new String[] {"Identifier"};
        case -555382895: /*parentSubstanceName*/ return new String[] {"string"};
        case 57176467: /*countryOfOrigin*/ return new String[] {"CodeableConcept"};
        case -1988836681: /*geographicalLocation*/ return new String[] {"string"};
        case 391529091: /*developmentStage*/ return new String[] {"CodeableConcept"};
        case 1472689306: /*fractionDescription*/ return new String[] {};
        case 1316389074: /*organism*/ return new String[] {};
        case -1803623927: /*partDescription*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sourceMaterialClass")) {
          this.sourceMaterialClass = new CodeableConcept();
          return this.sourceMaterialClass;
        }
        else if (name.equals("sourceMaterialType")) {
          this.sourceMaterialType = new CodeableConcept();
          return this.sourceMaterialType;
        }
        else if (name.equals("sourceMaterialState")) {
          this.sourceMaterialState = new CodeableConcept();
          return this.sourceMaterialState;
        }
        else if (name.equals("organismId")) {
          this.organismId = new Identifier();
          return this.organismId;
        }
        else if (name.equals("organismName")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.organismName");
        }
        else if (name.equals("parentSubstanceId")) {
          return addParentSubstanceId();
        }
        else if (name.equals("parentSubstanceName")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.parentSubstanceName");
        }
        else if (name.equals("countryOfOrigin")) {
          return addCountryOfOrigin();
        }
        else if (name.equals("geographicalLocation")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSourceMaterial.geographicalLocation");
        }
        else if (name.equals("developmentStage")) {
          this.developmentStage = new CodeableConcept();
          return this.developmentStage;
        }
        else if (name.equals("fractionDescription")) {
          return addFractionDescription();
        }
        else if (name.equals("organism")) {
          this.organism = new SubstanceSourceMaterialOrganismComponent();
          return this.organism;
        }
        else if (name.equals("partDescription")) {
          return addPartDescription();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstanceSourceMaterial";

  }

      public SubstanceSourceMaterial copy() {
        SubstanceSourceMaterial dst = new SubstanceSourceMaterial();
        copyValues(dst);
        dst.sourceMaterialClass = sourceMaterialClass == null ? null : sourceMaterialClass.copy();
        dst.sourceMaterialType = sourceMaterialType == null ? null : sourceMaterialType.copy();
        dst.sourceMaterialState = sourceMaterialState == null ? null : sourceMaterialState.copy();
        dst.organismId = organismId == null ? null : organismId.copy();
        dst.organismName = organismName == null ? null : organismName.copy();
        if (parentSubstanceId != null) {
          dst.parentSubstanceId = new ArrayList<Identifier>();
          for (Identifier i : parentSubstanceId)
            dst.parentSubstanceId.add(i.copy());
        };
        if (parentSubstanceName != null) {
          dst.parentSubstanceName = new ArrayList<StringType>();
          for (StringType i : parentSubstanceName)
            dst.parentSubstanceName.add(i.copy());
        };
        if (countryOfOrigin != null) {
          dst.countryOfOrigin = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : countryOfOrigin)
            dst.countryOfOrigin.add(i.copy());
        };
        if (geographicalLocation != null) {
          dst.geographicalLocation = new ArrayList<StringType>();
          for (StringType i : geographicalLocation)
            dst.geographicalLocation.add(i.copy());
        };
        dst.developmentStage = developmentStage == null ? null : developmentStage.copy();
        if (fractionDescription != null) {
          dst.fractionDescription = new ArrayList<SubstanceSourceMaterialFractionDescriptionComponent>();
          for (SubstanceSourceMaterialFractionDescriptionComponent i : fractionDescription)
            dst.fractionDescription.add(i.copy());
        };
        dst.organism = organism == null ? null : organism.copy();
        if (partDescription != null) {
          dst.partDescription = new ArrayList<SubstanceSourceMaterialPartDescriptionComponent>();
          for (SubstanceSourceMaterialPartDescriptionComponent i : partDescription)
            dst.partDescription.add(i.copy());
        };
        return dst;
      }

      protected SubstanceSourceMaterial typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterial))
          return false;
        SubstanceSourceMaterial o = (SubstanceSourceMaterial) other_;
        return compareDeep(sourceMaterialClass, o.sourceMaterialClass, true) && compareDeep(sourceMaterialType, o.sourceMaterialType, true)
           && compareDeep(sourceMaterialState, o.sourceMaterialState, true) && compareDeep(organismId, o.organismId, true)
           && compareDeep(organismName, o.organismName, true) && compareDeep(parentSubstanceId, o.parentSubstanceId, true)
           && compareDeep(parentSubstanceName, o.parentSubstanceName, true) && compareDeep(countryOfOrigin, o.countryOfOrigin, true)
           && compareDeep(geographicalLocation, o.geographicalLocation, true) && compareDeep(developmentStage, o.developmentStage, true)
           && compareDeep(fractionDescription, o.fractionDescription, true) && compareDeep(organism, o.organism, true)
           && compareDeep(partDescription, o.partDescription, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSourceMaterial))
          return false;
        SubstanceSourceMaterial o = (SubstanceSourceMaterial) other_;
        return compareValues(organismName, o.organismName, true) && compareValues(parentSubstanceName, o.parentSubstanceName, true)
           && compareValues(geographicalLocation, o.geographicalLocation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sourceMaterialClass, sourceMaterialType
          , sourceMaterialState, organismId, organismName, parentSubstanceId, parentSubstanceName
          , countryOfOrigin, geographicalLocation, developmentStage, fractionDescription, organism
          , partDescription);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SubstanceSourceMaterial;
   }


}

