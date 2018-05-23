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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

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
 * Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).
 */
@ResourceDef(name="MedicinalProduct", profile="http://hl7.org/fhir/Profile/MedicinalProduct")
public class MedicinalProduct extends DomainResource {

    @Block()
    public static class MedicinalProductNameComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The full product name.
         */
        @Child(name = "fullName", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The full product name", formalDefinition="The full product name." )
        protected StringType fullName;

        /**
         * Coding words or phrases of the name.
         */
        @Child(name = "namePart", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Coding words or phrases of the name", formalDefinition="Coding words or phrases of the name." )
        protected List<MedicinalProductNameNamePartComponent> namePart;

        /**
         * Country where the name applies.
         */
        @Child(name = "countryLanguage", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Country where the name applies", formalDefinition="Country where the name applies." )
        protected List<MedicinalProductNameCountryLanguageComponent> countryLanguage;

        private static final long serialVersionUID = 993877853L;

    /**
     * Constructor
     */
      public MedicinalProductNameComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductNameComponent(StringType fullName) {
        super();
        this.fullName = fullName;
      }

        /**
         * @return {@link #fullName} (The full product name.). This is the underlying object with id, value and extensions. The accessor "getFullName" gives direct access to the value
         */
        public StringType getFullNameElement() { 
          if (this.fullName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductNameComponent.fullName");
            else if (Configuration.doAutoCreate())
              this.fullName = new StringType(); // bb
          return this.fullName;
        }

        public boolean hasFullNameElement() { 
          return this.fullName != null && !this.fullName.isEmpty();
        }

        public boolean hasFullName() { 
          return this.fullName != null && !this.fullName.isEmpty();
        }

        /**
         * @param value {@link #fullName} (The full product name.). This is the underlying object with id, value and extensions. The accessor "getFullName" gives direct access to the value
         */
        public MedicinalProductNameComponent setFullNameElement(StringType value) { 
          this.fullName = value;
          return this;
        }

        /**
         * @return The full product name.
         */
        public String getFullName() { 
          return this.fullName == null ? null : this.fullName.getValue();
        }

        /**
         * @param value The full product name.
         */
        public MedicinalProductNameComponent setFullName(String value) { 
            if (this.fullName == null)
              this.fullName = new StringType();
            this.fullName.setValue(value);
          return this;
        }

        /**
         * @return {@link #namePart} (Coding words or phrases of the name.)
         */
        public List<MedicinalProductNameNamePartComponent> getNamePart() { 
          if (this.namePart == null)
            this.namePart = new ArrayList<MedicinalProductNameNamePartComponent>();
          return this.namePart;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductNameComponent setNamePart(List<MedicinalProductNameNamePartComponent> theNamePart) { 
          this.namePart = theNamePart;
          return this;
        }

        public boolean hasNamePart() { 
          if (this.namePart == null)
            return false;
          for (MedicinalProductNameNamePartComponent item : this.namePart)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductNameNamePartComponent addNamePart() { //3
          MedicinalProductNameNamePartComponent t = new MedicinalProductNameNamePartComponent();
          if (this.namePart == null)
            this.namePart = new ArrayList<MedicinalProductNameNamePartComponent>();
          this.namePart.add(t);
          return t;
        }

        public MedicinalProductNameComponent addNamePart(MedicinalProductNameNamePartComponent t) { //3
          if (t == null)
            return this;
          if (this.namePart == null)
            this.namePart = new ArrayList<MedicinalProductNameNamePartComponent>();
          this.namePart.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #namePart}, creating it if it does not already exist
         */
        public MedicinalProductNameNamePartComponent getNamePartFirstRep() { 
          if (getNamePart().isEmpty()) {
            addNamePart();
          }
          return getNamePart().get(0);
        }

        /**
         * @return {@link #countryLanguage} (Country where the name applies.)
         */
        public List<MedicinalProductNameCountryLanguageComponent> getCountryLanguage() { 
          if (this.countryLanguage == null)
            this.countryLanguage = new ArrayList<MedicinalProductNameCountryLanguageComponent>();
          return this.countryLanguage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductNameComponent setCountryLanguage(List<MedicinalProductNameCountryLanguageComponent> theCountryLanguage) { 
          this.countryLanguage = theCountryLanguage;
          return this;
        }

        public boolean hasCountryLanguage() { 
          if (this.countryLanguage == null)
            return false;
          for (MedicinalProductNameCountryLanguageComponent item : this.countryLanguage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductNameCountryLanguageComponent addCountryLanguage() { //3
          MedicinalProductNameCountryLanguageComponent t = new MedicinalProductNameCountryLanguageComponent();
          if (this.countryLanguage == null)
            this.countryLanguage = new ArrayList<MedicinalProductNameCountryLanguageComponent>();
          this.countryLanguage.add(t);
          return t;
        }

        public MedicinalProductNameComponent addCountryLanguage(MedicinalProductNameCountryLanguageComponent t) { //3
          if (t == null)
            return this;
          if (this.countryLanguage == null)
            this.countryLanguage = new ArrayList<MedicinalProductNameCountryLanguageComponent>();
          this.countryLanguage.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #countryLanguage}, creating it if it does not already exist
         */
        public MedicinalProductNameCountryLanguageComponent getCountryLanguageFirstRep() { 
          if (getCountryLanguage().isEmpty()) {
            addCountryLanguage();
          }
          return getCountryLanguage().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("fullName", "string", "The full product name.", 0, 1, fullName));
          children.add(new Property("namePart", "", "Coding words or phrases of the name.", 0, java.lang.Integer.MAX_VALUE, namePart));
          children.add(new Property("countryLanguage", "", "Country where the name applies.", 0, java.lang.Integer.MAX_VALUE, countryLanguage));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1330852282: /*fullName*/  return new Property("fullName", "string", "The full product name.", 0, 1, fullName);
          case 1840452894: /*namePart*/  return new Property("namePart", "", "Coding words or phrases of the name.", 0, java.lang.Integer.MAX_VALUE, namePart);
          case -141141746: /*countryLanguage*/  return new Property("countryLanguage", "", "Country where the name applies.", 0, java.lang.Integer.MAX_VALUE, countryLanguage);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1330852282: /*fullName*/ return this.fullName == null ? new Base[0] : new Base[] {this.fullName}; // StringType
        case 1840452894: /*namePart*/ return this.namePart == null ? new Base[0] : this.namePart.toArray(new Base[this.namePart.size()]); // MedicinalProductNameNamePartComponent
        case -141141746: /*countryLanguage*/ return this.countryLanguage == null ? new Base[0] : this.countryLanguage.toArray(new Base[this.countryLanguage.size()]); // MedicinalProductNameCountryLanguageComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1330852282: // fullName
          this.fullName = castToString(value); // StringType
          return value;
        case 1840452894: // namePart
          this.getNamePart().add((MedicinalProductNameNamePartComponent) value); // MedicinalProductNameNamePartComponent
          return value;
        case -141141746: // countryLanguage
          this.getCountryLanguage().add((MedicinalProductNameCountryLanguageComponent) value); // MedicinalProductNameCountryLanguageComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("fullName")) {
          this.fullName = castToString(value); // StringType
        } else if (name.equals("namePart")) {
          this.getNamePart().add((MedicinalProductNameNamePartComponent) value);
        } else if (name.equals("countryLanguage")) {
          this.getCountryLanguage().add((MedicinalProductNameCountryLanguageComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1330852282:  return getFullNameElement();
        case 1840452894:  return addNamePart(); 
        case -141141746:  return addCountryLanguage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1330852282: /*fullName*/ return new String[] {"string"};
        case 1840452894: /*namePart*/ return new String[] {};
        case -141141746: /*countryLanguage*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("fullName")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProduct.fullName");
        }
        else if (name.equals("namePart")) {
          return addNamePart();
        }
        else if (name.equals("countryLanguage")) {
          return addCountryLanguage();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductNameComponent copy() {
        MedicinalProductNameComponent dst = new MedicinalProductNameComponent();
        copyValues(dst);
        dst.fullName = fullName == null ? null : fullName.copy();
        if (namePart != null) {
          dst.namePart = new ArrayList<MedicinalProductNameNamePartComponent>();
          for (MedicinalProductNameNamePartComponent i : namePart)
            dst.namePart.add(i.copy());
        };
        if (countryLanguage != null) {
          dst.countryLanguage = new ArrayList<MedicinalProductNameCountryLanguageComponent>();
          for (MedicinalProductNameCountryLanguageComponent i : countryLanguage)
            dst.countryLanguage.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductNameComponent))
          return false;
        MedicinalProductNameComponent o = (MedicinalProductNameComponent) other_;
        return compareDeep(fullName, o.fullName, true) && compareDeep(namePart, o.namePart, true) && compareDeep(countryLanguage, o.countryLanguage, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductNameComponent))
          return false;
        MedicinalProductNameComponent o = (MedicinalProductNameComponent) other_;
        return compareValues(fullName, o.fullName, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(fullName, namePart, countryLanguage
          );
      }

  public String fhirType() {
    return "MedicinalProduct.name";

  }

  }

    @Block()
    public static class MedicinalProductNameNamePartComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A fragment of a product name.
         */
        @Child(name = "part", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A fragment of a product name", formalDefinition="A fragment of a product name." )
        protected StringType part;

        /**
         * Idenifying type for this part of the name (e.g. strength part).
         */
        @Child(name = "type", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Idenifying type for this part of the name (e.g. strength part)", formalDefinition="Idenifying type for this part of the name (e.g. strength part)." )
        protected Coding type;

        private static final long serialVersionUID = -301533796L;

    /**
     * Constructor
     */
      public MedicinalProductNameNamePartComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductNameNamePartComponent(StringType part, Coding type) {
        super();
        this.part = part;
        this.type = type;
      }

        /**
         * @return {@link #part} (A fragment of a product name.). This is the underlying object with id, value and extensions. The accessor "getPart" gives direct access to the value
         */
        public StringType getPartElement() { 
          if (this.part == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductNameNamePartComponent.part");
            else if (Configuration.doAutoCreate())
              this.part = new StringType(); // bb
          return this.part;
        }

        public boolean hasPartElement() { 
          return this.part != null && !this.part.isEmpty();
        }

        public boolean hasPart() { 
          return this.part != null && !this.part.isEmpty();
        }

        /**
         * @param value {@link #part} (A fragment of a product name.). This is the underlying object with id, value and extensions. The accessor "getPart" gives direct access to the value
         */
        public MedicinalProductNameNamePartComponent setPartElement(StringType value) { 
          this.part = value;
          return this;
        }

        /**
         * @return A fragment of a product name.
         */
        public String getPart() { 
          return this.part == null ? null : this.part.getValue();
        }

        /**
         * @param value A fragment of a product name.
         */
        public MedicinalProductNameNamePartComponent setPart(String value) { 
            if (this.part == null)
              this.part = new StringType();
            this.part.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (Idenifying type for this part of the name (e.g. strength part).)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductNameNamePartComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Idenifying type for this part of the name (e.g. strength part).)
         */
        public MedicinalProductNameNamePartComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("part", "string", "A fragment of a product name.", 0, 1, part));
          children.add(new Property("type", "Coding", "Idenifying type for this part of the name (e.g. strength part).", 0, 1, type));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3433459: /*part*/  return new Property("part", "string", "A fragment of a product name.", 0, 1, part);
          case 3575610: /*type*/  return new Property("type", "Coding", "Idenifying type for this part of the name (e.g. strength part).", 0, 1, type);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433459: /*part*/ return this.part == null ? new Base[0] : new Base[] {this.part}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433459: // part
          this.part = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("part")) {
          this.part = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCoding(value); // Coding
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433459:  return getPartElement();
        case 3575610:  return getType(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433459: /*part*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("part")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProduct.part");
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductNameNamePartComponent copy() {
        MedicinalProductNameNamePartComponent dst = new MedicinalProductNameNamePartComponent();
        copyValues(dst);
        dst.part = part == null ? null : part.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductNameNamePartComponent))
          return false;
        MedicinalProductNameNamePartComponent o = (MedicinalProductNameNamePartComponent) other_;
        return compareDeep(part, o.part, true) && compareDeep(type, o.type, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductNameNamePartComponent))
          return false;
        MedicinalProductNameNamePartComponent o = (MedicinalProductNameNamePartComponent) other_;
        return compareValues(part, o.part, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(part, type);
      }

  public String fhirType() {
    return "MedicinalProduct.name.namePart";

  }

  }

    @Block()
    public static class MedicinalProductNameCountryLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Country code for where this name applies.
         */
        @Child(name = "country", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Country code for where this name applies", formalDefinition="Country code for where this name applies." )
        protected CodeableConcept country;

        /**
         * Jurisdiction code for where this name applies.
         */
        @Child(name = "jurisdiction", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Jurisdiction code for where this name applies", formalDefinition="Jurisdiction code for where this name applies." )
        protected CodeableConcept jurisdiction;

        /**
         * Language code for this name.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Language code for this name", formalDefinition="Language code for this name." )
        protected CodeableConcept language;

        private static final long serialVersionUID = 1627157564L;

    /**
     * Constructor
     */
      public MedicinalProductNameCountryLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductNameCountryLanguageComponent(CodeableConcept country, CodeableConcept language) {
        super();
        this.country = country;
        this.language = language;
      }

        /**
         * @return {@link #country} (Country code for where this name applies.)
         */
        public CodeableConcept getCountry() { 
          if (this.country == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductNameCountryLanguageComponent.country");
            else if (Configuration.doAutoCreate())
              this.country = new CodeableConcept(); // cc
          return this.country;
        }

        public boolean hasCountry() { 
          return this.country != null && !this.country.isEmpty();
        }

        /**
         * @param value {@link #country} (Country code for where this name applies.)
         */
        public MedicinalProductNameCountryLanguageComponent setCountry(CodeableConcept value) { 
          this.country = value;
          return this;
        }

        /**
         * @return {@link #jurisdiction} (Jurisdiction code for where this name applies.)
         */
        public CodeableConcept getJurisdiction() { 
          if (this.jurisdiction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductNameCountryLanguageComponent.jurisdiction");
            else if (Configuration.doAutoCreate())
              this.jurisdiction = new CodeableConcept(); // cc
          return this.jurisdiction;
        }

        public boolean hasJurisdiction() { 
          return this.jurisdiction != null && !this.jurisdiction.isEmpty();
        }

        /**
         * @param value {@link #jurisdiction} (Jurisdiction code for where this name applies.)
         */
        public MedicinalProductNameCountryLanguageComponent setJurisdiction(CodeableConcept value) { 
          this.jurisdiction = value;
          return this;
        }

        /**
         * @return {@link #language} (Language code for this name.)
         */
        public CodeableConcept getLanguage() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductNameCountryLanguageComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeableConcept(); // cc
          return this.language;
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (Language code for this name.)
         */
        public MedicinalProductNameCountryLanguageComponent setLanguage(CodeableConcept value) { 
          this.language = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("country", "CodeableConcept", "Country code for where this name applies.", 0, 1, country));
          children.add(new Property("jurisdiction", "CodeableConcept", "Jurisdiction code for where this name applies.", 0, 1, jurisdiction));
          children.add(new Property("language", "CodeableConcept", "Language code for this name.", 0, 1, language));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 957831062: /*country*/  return new Property("country", "CodeableConcept", "Country code for where this name applies.", 0, 1, country);
          case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "Jurisdiction code for where this name applies.", 0, 1, jurisdiction);
          case -1613589672: /*language*/  return new Property("language", "CodeableConcept", "Language code for this name.", 0, 1, language);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 957831062: /*country*/ return this.country == null ? new Base[0] : new Base[] {this.country}; // CodeableConcept
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : new Base[] {this.jurisdiction}; // CodeableConcept
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 957831062: // country
          this.country = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -507075711: // jurisdiction
          this.jurisdiction = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1613589672: // language
          this.language = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("country")) {
          this.country = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("jurisdiction")) {
          this.jurisdiction = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("language")) {
          this.language = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 957831062:  return getCountry(); 
        case -507075711:  return getJurisdiction(); 
        case -1613589672:  return getLanguage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 957831062: /*country*/ return new String[] {"CodeableConcept"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -1613589672: /*language*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("country")) {
          this.country = new CodeableConcept();
          return this.country;
        }
        else if (name.equals("jurisdiction")) {
          this.jurisdiction = new CodeableConcept();
          return this.jurisdiction;
        }
        else if (name.equals("language")) {
          this.language = new CodeableConcept();
          return this.language;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductNameCountryLanguageComponent copy() {
        MedicinalProductNameCountryLanguageComponent dst = new MedicinalProductNameCountryLanguageComponent();
        copyValues(dst);
        dst.country = country == null ? null : country.copy();
        dst.jurisdiction = jurisdiction == null ? null : jurisdiction.copy();
        dst.language = language == null ? null : language.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductNameCountryLanguageComponent))
          return false;
        MedicinalProductNameCountryLanguageComponent o = (MedicinalProductNameCountryLanguageComponent) other_;
        return compareDeep(country, o.country, true) && compareDeep(jurisdiction, o.jurisdiction, true)
           && compareDeep(language, o.language, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductNameCountryLanguageComponent))
          return false;
        MedicinalProductNameCountryLanguageComponent o = (MedicinalProductNameCountryLanguageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(country, jurisdiction, language
          );
      }

  public String fhirType() {
    return "MedicinalProduct.name.countryLanguage";

  }

  }

    @Block()
    public static class MedicinalProductManufacturingBusinessOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of manufacturing operation.
         */
        @Child(name = "operationType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of manufacturing operation", formalDefinition="The type of manufacturing operation." )
        protected CodeableConcept operationType;

        /**
         * Regulatory authorization reference number.
         */
        @Child(name = "authorisationReferenceNumber", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Regulatory authorization reference number", formalDefinition="Regulatory authorization reference number." )
        protected Identifier authorisationReferenceNumber;

        /**
         * Regulatory authorization date.
         */
        @Child(name = "effectiveDate", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Regulatory authorization date", formalDefinition="Regulatory authorization date." )
        protected DateTimeType effectiveDate;

        /**
         * To indicate if this proces is commercially confidential.
         */
        @Child(name = "confidentialityIndicator", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="To indicate if this proces is commercially confidential", formalDefinition="To indicate if this proces is commercially confidential." )
        protected CodeableConcept confidentialityIndicator;

        /**
         * The manufacturer or establishment associated with the process.
         */
        @Child(name = "manufacturer", type = {Organization.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The manufacturer or establishment associated with the process", formalDefinition="The manufacturer or establishment associated with the process." )
        protected List<Reference> manufacturer;
        /**
         * The actual objects that are the target of the reference (The manufacturer or establishment associated with the process.)
         */
        protected List<Organization> manufacturerTarget;


        /**
         * A regulator which oversees the operation.
         */
        @Child(name = "regulator", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A regulator which oversees the operation", formalDefinition="A regulator which oversees the operation." )
        protected Reference regulator;

        /**
         * The actual object that is the target of the reference (A regulator which oversees the operation.)
         */
        protected Organization regulatorTarget;

        private static final long serialVersionUID = 1259822353L;

    /**
     * Constructor
     */
      public MedicinalProductManufacturingBusinessOperationComponent() {
        super();
      }

        /**
         * @return {@link #operationType} (The type of manufacturing operation.)
         */
        public CodeableConcept getOperationType() { 
          if (this.operationType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductManufacturingBusinessOperationComponent.operationType");
            else if (Configuration.doAutoCreate())
              this.operationType = new CodeableConcept(); // cc
          return this.operationType;
        }

        public boolean hasOperationType() { 
          return this.operationType != null && !this.operationType.isEmpty();
        }

        /**
         * @param value {@link #operationType} (The type of manufacturing operation.)
         */
        public MedicinalProductManufacturingBusinessOperationComponent setOperationType(CodeableConcept value) { 
          this.operationType = value;
          return this;
        }

        /**
         * @return {@link #authorisationReferenceNumber} (Regulatory authorization reference number.)
         */
        public Identifier getAuthorisationReferenceNumber() { 
          if (this.authorisationReferenceNumber == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductManufacturingBusinessOperationComponent.authorisationReferenceNumber");
            else if (Configuration.doAutoCreate())
              this.authorisationReferenceNumber = new Identifier(); // cc
          return this.authorisationReferenceNumber;
        }

        public boolean hasAuthorisationReferenceNumber() { 
          return this.authorisationReferenceNumber != null && !this.authorisationReferenceNumber.isEmpty();
        }

        /**
         * @param value {@link #authorisationReferenceNumber} (Regulatory authorization reference number.)
         */
        public MedicinalProductManufacturingBusinessOperationComponent setAuthorisationReferenceNumber(Identifier value) { 
          this.authorisationReferenceNumber = value;
          return this;
        }

        /**
         * @return {@link #effectiveDate} (Regulatory authorization date.). This is the underlying object with id, value and extensions. The accessor "getEffectiveDate" gives direct access to the value
         */
        public DateTimeType getEffectiveDateElement() { 
          if (this.effectiveDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductManufacturingBusinessOperationComponent.effectiveDate");
            else if (Configuration.doAutoCreate())
              this.effectiveDate = new DateTimeType(); // bb
          return this.effectiveDate;
        }

        public boolean hasEffectiveDateElement() { 
          return this.effectiveDate != null && !this.effectiveDate.isEmpty();
        }

        public boolean hasEffectiveDate() { 
          return this.effectiveDate != null && !this.effectiveDate.isEmpty();
        }

        /**
         * @param value {@link #effectiveDate} (Regulatory authorization date.). This is the underlying object with id, value and extensions. The accessor "getEffectiveDate" gives direct access to the value
         */
        public MedicinalProductManufacturingBusinessOperationComponent setEffectiveDateElement(DateTimeType value) { 
          this.effectiveDate = value;
          return this;
        }

        /**
         * @return Regulatory authorization date.
         */
        public Date getEffectiveDate() { 
          return this.effectiveDate == null ? null : this.effectiveDate.getValue();
        }

        /**
         * @param value Regulatory authorization date.
         */
        public MedicinalProductManufacturingBusinessOperationComponent setEffectiveDate(Date value) { 
          if (value == null)
            this.effectiveDate = null;
          else {
            if (this.effectiveDate == null)
              this.effectiveDate = new DateTimeType();
            this.effectiveDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #confidentialityIndicator} (To indicate if this proces is commercially confidential.)
         */
        public CodeableConcept getConfidentialityIndicator() { 
          if (this.confidentialityIndicator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductManufacturingBusinessOperationComponent.confidentialityIndicator");
            else if (Configuration.doAutoCreate())
              this.confidentialityIndicator = new CodeableConcept(); // cc
          return this.confidentialityIndicator;
        }

        public boolean hasConfidentialityIndicator() { 
          return this.confidentialityIndicator != null && !this.confidentialityIndicator.isEmpty();
        }

        /**
         * @param value {@link #confidentialityIndicator} (To indicate if this proces is commercially confidential.)
         */
        public MedicinalProductManufacturingBusinessOperationComponent setConfidentialityIndicator(CodeableConcept value) { 
          this.confidentialityIndicator = value;
          return this;
        }

        /**
         * @return {@link #manufacturer} (The manufacturer or establishment associated with the process.)
         */
        public List<Reference> getManufacturer() { 
          if (this.manufacturer == null)
            this.manufacturer = new ArrayList<Reference>();
          return this.manufacturer;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductManufacturingBusinessOperationComponent setManufacturer(List<Reference> theManufacturer) { 
          this.manufacturer = theManufacturer;
          return this;
        }

        public boolean hasManufacturer() { 
          if (this.manufacturer == null)
            return false;
          for (Reference item : this.manufacturer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addManufacturer() { //3
          Reference t = new Reference();
          if (this.manufacturer == null)
            this.manufacturer = new ArrayList<Reference>();
          this.manufacturer.add(t);
          return t;
        }

        public MedicinalProductManufacturingBusinessOperationComponent addManufacturer(Reference t) { //3
          if (t == null)
            return this;
          if (this.manufacturer == null)
            this.manufacturer = new ArrayList<Reference>();
          this.manufacturer.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #manufacturer}, creating it if it does not already exist
         */
        public Reference getManufacturerFirstRep() { 
          if (getManufacturer().isEmpty()) {
            addManufacturer();
          }
          return getManufacturer().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Organization> getManufacturerTarget() { 
          if (this.manufacturerTarget == null)
            this.manufacturerTarget = new ArrayList<Organization>();
          return this.manufacturerTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Organization addManufacturerTarget() { 
          Organization r = new Organization();
          if (this.manufacturerTarget == null)
            this.manufacturerTarget = new ArrayList<Organization>();
          this.manufacturerTarget.add(r);
          return r;
        }

        /**
         * @return {@link #regulator} (A regulator which oversees the operation.)
         */
        public Reference getRegulator() { 
          if (this.regulator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductManufacturingBusinessOperationComponent.regulator");
            else if (Configuration.doAutoCreate())
              this.regulator = new Reference(); // cc
          return this.regulator;
        }

        public boolean hasRegulator() { 
          return this.regulator != null && !this.regulator.isEmpty();
        }

        /**
         * @param value {@link #regulator} (A regulator which oversees the operation.)
         */
        public MedicinalProductManufacturingBusinessOperationComponent setRegulator(Reference value) { 
          this.regulator = value;
          return this;
        }

        /**
         * @return {@link #regulator} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A regulator which oversees the operation.)
         */
        public Organization getRegulatorTarget() { 
          if (this.regulatorTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductManufacturingBusinessOperationComponent.regulator");
            else if (Configuration.doAutoCreate())
              this.regulatorTarget = new Organization(); // aa
          return this.regulatorTarget;
        }

        /**
         * @param value {@link #regulator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A regulator which oversees the operation.)
         */
        public MedicinalProductManufacturingBusinessOperationComponent setRegulatorTarget(Organization value) { 
          this.regulatorTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("operationType", "CodeableConcept", "The type of manufacturing operation.", 0, 1, operationType));
          children.add(new Property("authorisationReferenceNumber", "Identifier", "Regulatory authorization reference number.", 0, 1, authorisationReferenceNumber));
          children.add(new Property("effectiveDate", "dateTime", "Regulatory authorization date.", 0, 1, effectiveDate));
          children.add(new Property("confidentialityIndicator", "CodeableConcept", "To indicate if this proces is commercially confidential.", 0, 1, confidentialityIndicator));
          children.add(new Property("manufacturer", "Reference(Organization)", "The manufacturer or establishment associated with the process.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
          children.add(new Property("regulator", "Reference(Organization)", "A regulator which oversees the operation.", 0, 1, regulator));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 91999553: /*operationType*/  return new Property("operationType", "CodeableConcept", "The type of manufacturing operation.", 0, 1, operationType);
          case -1940839884: /*authorisationReferenceNumber*/  return new Property("authorisationReferenceNumber", "Identifier", "Regulatory authorization reference number.", 0, 1, authorisationReferenceNumber);
          case -930389515: /*effectiveDate*/  return new Property("effectiveDate", "dateTime", "Regulatory authorization date.", 0, 1, effectiveDate);
          case -1449404791: /*confidentialityIndicator*/  return new Property("confidentialityIndicator", "CodeableConcept", "To indicate if this proces is commercially confidential.", 0, 1, confidentialityIndicator);
          case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "The manufacturer or establishment associated with the process.", 0, java.lang.Integer.MAX_VALUE, manufacturer);
          case 414760449: /*regulator*/  return new Property("regulator", "Reference(Organization)", "A regulator which oversees the operation.", 0, 1, regulator);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 91999553: /*operationType*/ return this.operationType == null ? new Base[0] : new Base[] {this.operationType}; // CodeableConcept
        case -1940839884: /*authorisationReferenceNumber*/ return this.authorisationReferenceNumber == null ? new Base[0] : new Base[] {this.authorisationReferenceNumber}; // Identifier
        case -930389515: /*effectiveDate*/ return this.effectiveDate == null ? new Base[0] : new Base[] {this.effectiveDate}; // DateTimeType
        case -1449404791: /*confidentialityIndicator*/ return this.confidentialityIndicator == null ? new Base[0] : new Base[] {this.confidentialityIndicator}; // CodeableConcept
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : this.manufacturer.toArray(new Base[this.manufacturer.size()]); // Reference
        case 414760449: /*regulator*/ return this.regulator == null ? new Base[0] : new Base[] {this.regulator}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 91999553: // operationType
          this.operationType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1940839884: // authorisationReferenceNumber
          this.authorisationReferenceNumber = castToIdentifier(value); // Identifier
          return value;
        case -930389515: // effectiveDate
          this.effectiveDate = castToDateTime(value); // DateTimeType
          return value;
        case -1449404791: // confidentialityIndicator
          this.confidentialityIndicator = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1969347631: // manufacturer
          this.getManufacturer().add(castToReference(value)); // Reference
          return value;
        case 414760449: // regulator
          this.regulator = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("operationType")) {
          this.operationType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("authorisationReferenceNumber")) {
          this.authorisationReferenceNumber = castToIdentifier(value); // Identifier
        } else if (name.equals("effectiveDate")) {
          this.effectiveDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("confidentialityIndicator")) {
          this.confidentialityIndicator = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("manufacturer")) {
          this.getManufacturer().add(castToReference(value));
        } else if (name.equals("regulator")) {
          this.regulator = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 91999553:  return getOperationType(); 
        case -1940839884:  return getAuthorisationReferenceNumber(); 
        case -930389515:  return getEffectiveDateElement();
        case -1449404791:  return getConfidentialityIndicator(); 
        case -1969347631:  return addManufacturer(); 
        case 414760449:  return getRegulator(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 91999553: /*operationType*/ return new String[] {"CodeableConcept"};
        case -1940839884: /*authorisationReferenceNumber*/ return new String[] {"Identifier"};
        case -930389515: /*effectiveDate*/ return new String[] {"dateTime"};
        case -1449404791: /*confidentialityIndicator*/ return new String[] {"CodeableConcept"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case 414760449: /*regulator*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("operationType")) {
          this.operationType = new CodeableConcept();
          return this.operationType;
        }
        else if (name.equals("authorisationReferenceNumber")) {
          this.authorisationReferenceNumber = new Identifier();
          return this.authorisationReferenceNumber;
        }
        else if (name.equals("effectiveDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProduct.effectiveDate");
        }
        else if (name.equals("confidentialityIndicator")) {
          this.confidentialityIndicator = new CodeableConcept();
          return this.confidentialityIndicator;
        }
        else if (name.equals("manufacturer")) {
          return addManufacturer();
        }
        else if (name.equals("regulator")) {
          this.regulator = new Reference();
          return this.regulator;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductManufacturingBusinessOperationComponent copy() {
        MedicinalProductManufacturingBusinessOperationComponent dst = new MedicinalProductManufacturingBusinessOperationComponent();
        copyValues(dst);
        dst.operationType = operationType == null ? null : operationType.copy();
        dst.authorisationReferenceNumber = authorisationReferenceNumber == null ? null : authorisationReferenceNumber.copy();
        dst.effectiveDate = effectiveDate == null ? null : effectiveDate.copy();
        dst.confidentialityIndicator = confidentialityIndicator == null ? null : confidentialityIndicator.copy();
        if (manufacturer != null) {
          dst.manufacturer = new ArrayList<Reference>();
          for (Reference i : manufacturer)
            dst.manufacturer.add(i.copy());
        };
        dst.regulator = regulator == null ? null : regulator.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductManufacturingBusinessOperationComponent))
          return false;
        MedicinalProductManufacturingBusinessOperationComponent o = (MedicinalProductManufacturingBusinessOperationComponent) other_;
        return compareDeep(operationType, o.operationType, true) && compareDeep(authorisationReferenceNumber, o.authorisationReferenceNumber, true)
           && compareDeep(effectiveDate, o.effectiveDate, true) && compareDeep(confidentialityIndicator, o.confidentialityIndicator, true)
           && compareDeep(manufacturer, o.manufacturer, true) && compareDeep(regulator, o.regulator, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductManufacturingBusinessOperationComponent))
          return false;
        MedicinalProductManufacturingBusinessOperationComponent o = (MedicinalProductManufacturingBusinessOperationComponent) other_;
        return compareValues(effectiveDate, o.effectiveDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(operationType, authorisationReferenceNumber
          , effectiveDate, confidentialityIndicator, manufacturer, regulator);
      }

  public String fhirType() {
    return "MedicinalProduct.manufacturingBusinessOperation";

  }

  }

    /**
     * Business idenfifier for this product. Could be an MPID.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business idenfifier for this product. Could be an MPID", formalDefinition="Business idenfifier for this product. Could be an MPID." )
    protected Identifier identifier;

    /**
     * Regulatory type, e.g. Investigational or Authorized.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Regulatory type, e.g. Investigational or Authorized", formalDefinition="Regulatory type, e.g. Investigational or Authorized." )
    protected CodeableConcept type;

    /**
     * The dose form for a single part product, or combined form of a multiple part product.
     */
    @Child(name = "combinedPharmaceuticalDoseForm", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The dose form for a single part product, or combined form of a multiple part product", formalDefinition="The dose form for a single part product, or combined form of a multiple part product." )
    protected CodeableConcept combinedPharmaceuticalDoseForm;

    /**
     * Whether the Medicinal Product is subject to additional monitoring for regulatory reasons.
     */
    @Child(name = "additionalMonitoringIndicator", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the Medicinal Product is subject to additional monitoring for regulatory reasons", formalDefinition="Whether the Medicinal Product is subject to additional monitoring for regulatory reasons." )
    protected CodeableConcept additionalMonitoringIndicator;

    /**
     * Whether the Medicinal Product is subject to special measures for regulatory reasons.
     */
    @Child(name = "specialMeasures", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Whether the Medicinal Product is subject to special measures for regulatory reasons", formalDefinition="Whether the Medicinal Product is subject to special measures for regulatory reasons." )
    protected List<StringType> specialMeasures;

    /**
     * If authorised for use in children.
     */
    @Child(name = "paediatricUseIndicator", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If authorised for use in children", formalDefinition="If authorised for use in children." )
    protected CodeableConcept paediatricUseIndicator;

    /**
     * Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.
     */
    @Child(name = "orphanDesignationStatus", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Indicates if the medicinal product has an orphan designation for the treatment of a rare disease", formalDefinition="Indicates if the medicinal product has an orphan designation for the treatment of a rare disease." )
    protected CodeableConcept orphanDesignationStatus;

    /**
     * Allows the product to be classified by various systems.
     */
    @Child(name = "productClassification", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Allows the product to be classified by various systems", formalDefinition="Allows the product to be classified by various systems." )
    protected List<CodeableConcept> productClassification;

    /**
     * Product regulatory authorization.
     */
    @Child(name = "marketingAuthorization", type = {MedicinalProductAuthorization.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Product regulatory authorization", formalDefinition="Product regulatory authorization." )
    protected Reference marketingAuthorization;

    /**
     * The actual object that is the target of the reference (Product regulatory authorization.)
     */
    protected MedicinalProductAuthorization marketingAuthorizationTarget;

    /**
     * Package representation for the product.
     */
    @Child(name = "packagedMedicinalProduct", type = {MedicinalProductPackaged.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Package representation for the product", formalDefinition="Package representation for the product." )
    protected List<Reference> packagedMedicinalProduct;
    /**
     * The actual objects that are the target of the reference (Package representation for the product.)
     */
    protected List<MedicinalProductPackaged> packagedMedicinalProductTarget;


    /**
     * Pharmaceutical aspects of product.
     */
    @Child(name = "pharmaceuticalProduct", type = {MedicinalProductPharmaceutical.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Pharmaceutical aspects of product", formalDefinition="Pharmaceutical aspects of product." )
    protected List<Reference> pharmaceuticalProduct;
    /**
     * The actual objects that are the target of the reference (Pharmaceutical aspects of product.)
     */
    protected List<MedicinalProductPharmaceutical> pharmaceuticalProductTarget;


    /**
     * Clinical particulars, indications etc.
     */
    @Child(name = "clinicalParticulars", type = {MedicinalProductClinicals.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Clinical particulars, indications etc.", formalDefinition="Clinical particulars, indications etc." )
    protected List<Reference> clinicalParticulars;
    /**
     * The actual objects that are the target of the reference (Clinical particulars, indications etc.)
     */
    protected List<MedicinalProductClinicals> clinicalParticularsTarget;


    /**
     * Supporting documentation, typically for regulatory submission.
     */
    @Child(name = "attachedDocument", type = {DocumentReference.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Supporting documentation, typically for regulatory submission", formalDefinition="Supporting documentation, typically for regulatory submission." )
    protected List<Reference> attachedDocument;
    /**
     * The actual objects that are the target of the reference (Supporting documentation, typically for regulatory submission.)
     */
    protected List<DocumentReference> attachedDocumentTarget;


    /**
     * A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).
     */
    @Child(name = "masterFile", type = {DocumentReference.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A master file for to the medicinal product (e.g. Pharmacovigilance System Master File)", formalDefinition="A master file for to the medicinal product (e.g. Pharmacovigilance System Master File)." )
    protected List<Reference> masterFile;
    /**
     * The actual objects that are the target of the reference (A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).)
     */
    protected List<DocumentReference> masterFileTarget;


    /**
     * The product's name, including full name and possibly coded parts.
     */
    @Child(name = "name", type = {}, order=14, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The product's name, including full name and possibly coded parts", formalDefinition="The product's name, including full name and possibly coded parts." )
    protected List<MedicinalProductNameComponent> name;

    /**
     * Reference to another product, e.g. for linking authorised to investigational product.
     */
    @Child(name = "crossReference", type = {Identifier.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reference to another product, e.g. for linking authorised to investigational product", formalDefinition="Reference to another product, e.g. for linking authorised to investigational product." )
    protected List<Identifier> crossReference;

    /**
     * An operation applied to the product, for manufacturing or adminsitrative purpose.
     */
    @Child(name = "manufacturingBusinessOperation", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An operation applied to the product, for manufacturing or adminsitrative purpose", formalDefinition="An operation applied to the product, for manufacturing or adminsitrative purpose." )
    protected List<MedicinalProductManufacturingBusinessOperationComponent> manufacturingBusinessOperation;

    private static final long serialVersionUID = -1694141448L;

  /**
   * Constructor
   */
    public MedicinalProduct() {
      super();
    }

  /**
   * Constructor
   */
    public MedicinalProduct(Identifier identifier) {
      super();
      this.identifier = identifier;
    }

    /**
     * @return {@link #identifier} (Business idenfifier for this product. Could be an MPID.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business idenfifier for this product. Could be an MPID.)
     */
    public MedicinalProduct setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #type} (Regulatory type, e.g. Investigational or Authorized.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Regulatory type, e.g. Investigational or Authorized.)
     */
    public MedicinalProduct setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #combinedPharmaceuticalDoseForm} (The dose form for a single part product, or combined form of a multiple part product.)
     */
    public CodeableConcept getCombinedPharmaceuticalDoseForm() { 
      if (this.combinedPharmaceuticalDoseForm == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.combinedPharmaceuticalDoseForm");
        else if (Configuration.doAutoCreate())
          this.combinedPharmaceuticalDoseForm = new CodeableConcept(); // cc
      return this.combinedPharmaceuticalDoseForm;
    }

    public boolean hasCombinedPharmaceuticalDoseForm() { 
      return this.combinedPharmaceuticalDoseForm != null && !this.combinedPharmaceuticalDoseForm.isEmpty();
    }

    /**
     * @param value {@link #combinedPharmaceuticalDoseForm} (The dose form for a single part product, or combined form of a multiple part product.)
     */
    public MedicinalProduct setCombinedPharmaceuticalDoseForm(CodeableConcept value) { 
      this.combinedPharmaceuticalDoseForm = value;
      return this;
    }

    /**
     * @return {@link #additionalMonitoringIndicator} (Whether the Medicinal Product is subject to additional monitoring for regulatory reasons.)
     */
    public CodeableConcept getAdditionalMonitoringIndicator() { 
      if (this.additionalMonitoringIndicator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.additionalMonitoringIndicator");
        else if (Configuration.doAutoCreate())
          this.additionalMonitoringIndicator = new CodeableConcept(); // cc
      return this.additionalMonitoringIndicator;
    }

    public boolean hasAdditionalMonitoringIndicator() { 
      return this.additionalMonitoringIndicator != null && !this.additionalMonitoringIndicator.isEmpty();
    }

    /**
     * @param value {@link #additionalMonitoringIndicator} (Whether the Medicinal Product is subject to additional monitoring for regulatory reasons.)
     */
    public MedicinalProduct setAdditionalMonitoringIndicator(CodeableConcept value) { 
      this.additionalMonitoringIndicator = value;
      return this;
    }

    /**
     * @return {@link #specialMeasures} (Whether the Medicinal Product is subject to special measures for regulatory reasons.)
     */
    public List<StringType> getSpecialMeasures() { 
      if (this.specialMeasures == null)
        this.specialMeasures = new ArrayList<StringType>();
      return this.specialMeasures;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setSpecialMeasures(List<StringType> theSpecialMeasures) { 
      this.specialMeasures = theSpecialMeasures;
      return this;
    }

    public boolean hasSpecialMeasures() { 
      if (this.specialMeasures == null)
        return false;
      for (StringType item : this.specialMeasures)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #specialMeasures} (Whether the Medicinal Product is subject to special measures for regulatory reasons.)
     */
    public StringType addSpecialMeasuresElement() {//2 
      StringType t = new StringType();
      if (this.specialMeasures == null)
        this.specialMeasures = new ArrayList<StringType>();
      this.specialMeasures.add(t);
      return t;
    }

    /**
     * @param value {@link #specialMeasures} (Whether the Medicinal Product is subject to special measures for regulatory reasons.)
     */
    public MedicinalProduct addSpecialMeasures(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.specialMeasures == null)
        this.specialMeasures = new ArrayList<StringType>();
      this.specialMeasures.add(t);
      return this;
    }

    /**
     * @param value {@link #specialMeasures} (Whether the Medicinal Product is subject to special measures for regulatory reasons.)
     */
    public boolean hasSpecialMeasures(String value) { 
      if (this.specialMeasures == null)
        return false;
      for (StringType v : this.specialMeasures)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #paediatricUseIndicator} (If authorised for use in children.)
     */
    public CodeableConcept getPaediatricUseIndicator() { 
      if (this.paediatricUseIndicator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.paediatricUseIndicator");
        else if (Configuration.doAutoCreate())
          this.paediatricUseIndicator = new CodeableConcept(); // cc
      return this.paediatricUseIndicator;
    }

    public boolean hasPaediatricUseIndicator() { 
      return this.paediatricUseIndicator != null && !this.paediatricUseIndicator.isEmpty();
    }

    /**
     * @param value {@link #paediatricUseIndicator} (If authorised for use in children.)
     */
    public MedicinalProduct setPaediatricUseIndicator(CodeableConcept value) { 
      this.paediatricUseIndicator = value;
      return this;
    }

    /**
     * @return {@link #orphanDesignationStatus} (Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.)
     */
    public CodeableConcept getOrphanDesignationStatus() { 
      if (this.orphanDesignationStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.orphanDesignationStatus");
        else if (Configuration.doAutoCreate())
          this.orphanDesignationStatus = new CodeableConcept(); // cc
      return this.orphanDesignationStatus;
    }

    public boolean hasOrphanDesignationStatus() { 
      return this.orphanDesignationStatus != null && !this.orphanDesignationStatus.isEmpty();
    }

    /**
     * @param value {@link #orphanDesignationStatus} (Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.)
     */
    public MedicinalProduct setOrphanDesignationStatus(CodeableConcept value) { 
      this.orphanDesignationStatus = value;
      return this;
    }

    /**
     * @return {@link #productClassification} (Allows the product to be classified by various systems.)
     */
    public List<CodeableConcept> getProductClassification() { 
      if (this.productClassification == null)
        this.productClassification = new ArrayList<CodeableConcept>();
      return this.productClassification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setProductClassification(List<CodeableConcept> theProductClassification) { 
      this.productClassification = theProductClassification;
      return this;
    }

    public boolean hasProductClassification() { 
      if (this.productClassification == null)
        return false;
      for (CodeableConcept item : this.productClassification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addProductClassification() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.productClassification == null)
        this.productClassification = new ArrayList<CodeableConcept>();
      this.productClassification.add(t);
      return t;
    }

    public MedicinalProduct addProductClassification(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.productClassification == null)
        this.productClassification = new ArrayList<CodeableConcept>();
      this.productClassification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #productClassification}, creating it if it does not already exist
     */
    public CodeableConcept getProductClassificationFirstRep() { 
      if (getProductClassification().isEmpty()) {
        addProductClassification();
      }
      return getProductClassification().get(0);
    }

    /**
     * @return {@link #marketingAuthorization} (Product regulatory authorization.)
     */
    public Reference getMarketingAuthorization() { 
      if (this.marketingAuthorization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.marketingAuthorization");
        else if (Configuration.doAutoCreate())
          this.marketingAuthorization = new Reference(); // cc
      return this.marketingAuthorization;
    }

    public boolean hasMarketingAuthorization() { 
      return this.marketingAuthorization != null && !this.marketingAuthorization.isEmpty();
    }

    /**
     * @param value {@link #marketingAuthorization} (Product regulatory authorization.)
     */
    public MedicinalProduct setMarketingAuthorization(Reference value) { 
      this.marketingAuthorization = value;
      return this;
    }

    /**
     * @return {@link #marketingAuthorization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Product regulatory authorization.)
     */
    public MedicinalProductAuthorization getMarketingAuthorizationTarget() { 
      if (this.marketingAuthorizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProduct.marketingAuthorization");
        else if (Configuration.doAutoCreate())
          this.marketingAuthorizationTarget = new MedicinalProductAuthorization(); // aa
      return this.marketingAuthorizationTarget;
    }

    /**
     * @param value {@link #marketingAuthorization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Product regulatory authorization.)
     */
    public MedicinalProduct setMarketingAuthorizationTarget(MedicinalProductAuthorization value) { 
      this.marketingAuthorizationTarget = value;
      return this;
    }

    /**
     * @return {@link #packagedMedicinalProduct} (Package representation for the product.)
     */
    public List<Reference> getPackagedMedicinalProduct() { 
      if (this.packagedMedicinalProduct == null)
        this.packagedMedicinalProduct = new ArrayList<Reference>();
      return this.packagedMedicinalProduct;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setPackagedMedicinalProduct(List<Reference> thePackagedMedicinalProduct) { 
      this.packagedMedicinalProduct = thePackagedMedicinalProduct;
      return this;
    }

    public boolean hasPackagedMedicinalProduct() { 
      if (this.packagedMedicinalProduct == null)
        return false;
      for (Reference item : this.packagedMedicinalProduct)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPackagedMedicinalProduct() { //3
      Reference t = new Reference();
      if (this.packagedMedicinalProduct == null)
        this.packagedMedicinalProduct = new ArrayList<Reference>();
      this.packagedMedicinalProduct.add(t);
      return t;
    }

    public MedicinalProduct addPackagedMedicinalProduct(Reference t) { //3
      if (t == null)
        return this;
      if (this.packagedMedicinalProduct == null)
        this.packagedMedicinalProduct = new ArrayList<Reference>();
      this.packagedMedicinalProduct.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #packagedMedicinalProduct}, creating it if it does not already exist
     */
    public Reference getPackagedMedicinalProductFirstRep() { 
      if (getPackagedMedicinalProduct().isEmpty()) {
        addPackagedMedicinalProduct();
      }
      return getPackagedMedicinalProduct().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProductPackaged> getPackagedMedicinalProductTarget() { 
      if (this.packagedMedicinalProductTarget == null)
        this.packagedMedicinalProductTarget = new ArrayList<MedicinalProductPackaged>();
      return this.packagedMedicinalProductTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProductPackaged addPackagedMedicinalProductTarget() { 
      MedicinalProductPackaged r = new MedicinalProductPackaged();
      if (this.packagedMedicinalProductTarget == null)
        this.packagedMedicinalProductTarget = new ArrayList<MedicinalProductPackaged>();
      this.packagedMedicinalProductTarget.add(r);
      return r;
    }

    /**
     * @return {@link #pharmaceuticalProduct} (Pharmaceutical aspects of product.)
     */
    public List<Reference> getPharmaceuticalProduct() { 
      if (this.pharmaceuticalProduct == null)
        this.pharmaceuticalProduct = new ArrayList<Reference>();
      return this.pharmaceuticalProduct;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setPharmaceuticalProduct(List<Reference> thePharmaceuticalProduct) { 
      this.pharmaceuticalProduct = thePharmaceuticalProduct;
      return this;
    }

    public boolean hasPharmaceuticalProduct() { 
      if (this.pharmaceuticalProduct == null)
        return false;
      for (Reference item : this.pharmaceuticalProduct)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPharmaceuticalProduct() { //3
      Reference t = new Reference();
      if (this.pharmaceuticalProduct == null)
        this.pharmaceuticalProduct = new ArrayList<Reference>();
      this.pharmaceuticalProduct.add(t);
      return t;
    }

    public MedicinalProduct addPharmaceuticalProduct(Reference t) { //3
      if (t == null)
        return this;
      if (this.pharmaceuticalProduct == null)
        this.pharmaceuticalProduct = new ArrayList<Reference>();
      this.pharmaceuticalProduct.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #pharmaceuticalProduct}, creating it if it does not already exist
     */
    public Reference getPharmaceuticalProductFirstRep() { 
      if (getPharmaceuticalProduct().isEmpty()) {
        addPharmaceuticalProduct();
      }
      return getPharmaceuticalProduct().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProductPharmaceutical> getPharmaceuticalProductTarget() { 
      if (this.pharmaceuticalProductTarget == null)
        this.pharmaceuticalProductTarget = new ArrayList<MedicinalProductPharmaceutical>();
      return this.pharmaceuticalProductTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProductPharmaceutical addPharmaceuticalProductTarget() { 
      MedicinalProductPharmaceutical r = new MedicinalProductPharmaceutical();
      if (this.pharmaceuticalProductTarget == null)
        this.pharmaceuticalProductTarget = new ArrayList<MedicinalProductPharmaceutical>();
      this.pharmaceuticalProductTarget.add(r);
      return r;
    }

    /**
     * @return {@link #clinicalParticulars} (Clinical particulars, indications etc.)
     */
    public List<Reference> getClinicalParticulars() { 
      if (this.clinicalParticulars == null)
        this.clinicalParticulars = new ArrayList<Reference>();
      return this.clinicalParticulars;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setClinicalParticulars(List<Reference> theClinicalParticulars) { 
      this.clinicalParticulars = theClinicalParticulars;
      return this;
    }

    public boolean hasClinicalParticulars() { 
      if (this.clinicalParticulars == null)
        return false;
      for (Reference item : this.clinicalParticulars)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addClinicalParticulars() { //3
      Reference t = new Reference();
      if (this.clinicalParticulars == null)
        this.clinicalParticulars = new ArrayList<Reference>();
      this.clinicalParticulars.add(t);
      return t;
    }

    public MedicinalProduct addClinicalParticulars(Reference t) { //3
      if (t == null)
        return this;
      if (this.clinicalParticulars == null)
        this.clinicalParticulars = new ArrayList<Reference>();
      this.clinicalParticulars.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #clinicalParticulars}, creating it if it does not already exist
     */
    public Reference getClinicalParticularsFirstRep() { 
      if (getClinicalParticulars().isEmpty()) {
        addClinicalParticulars();
      }
      return getClinicalParticulars().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProductClinicals> getClinicalParticularsTarget() { 
      if (this.clinicalParticularsTarget == null)
        this.clinicalParticularsTarget = new ArrayList<MedicinalProductClinicals>();
      return this.clinicalParticularsTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProductClinicals addClinicalParticularsTarget() { 
      MedicinalProductClinicals r = new MedicinalProductClinicals();
      if (this.clinicalParticularsTarget == null)
        this.clinicalParticularsTarget = new ArrayList<MedicinalProductClinicals>();
      this.clinicalParticularsTarget.add(r);
      return r;
    }

    /**
     * @return {@link #attachedDocument} (Supporting documentation, typically for regulatory submission.)
     */
    public List<Reference> getAttachedDocument() { 
      if (this.attachedDocument == null)
        this.attachedDocument = new ArrayList<Reference>();
      return this.attachedDocument;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setAttachedDocument(List<Reference> theAttachedDocument) { 
      this.attachedDocument = theAttachedDocument;
      return this;
    }

    public boolean hasAttachedDocument() { 
      if (this.attachedDocument == null)
        return false;
      for (Reference item : this.attachedDocument)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAttachedDocument() { //3
      Reference t = new Reference();
      if (this.attachedDocument == null)
        this.attachedDocument = new ArrayList<Reference>();
      this.attachedDocument.add(t);
      return t;
    }

    public MedicinalProduct addAttachedDocument(Reference t) { //3
      if (t == null)
        return this;
      if (this.attachedDocument == null)
        this.attachedDocument = new ArrayList<Reference>();
      this.attachedDocument.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #attachedDocument}, creating it if it does not already exist
     */
    public Reference getAttachedDocumentFirstRep() { 
      if (getAttachedDocument().isEmpty()) {
        addAttachedDocument();
      }
      return getAttachedDocument().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<DocumentReference> getAttachedDocumentTarget() { 
      if (this.attachedDocumentTarget == null)
        this.attachedDocumentTarget = new ArrayList<DocumentReference>();
      return this.attachedDocumentTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public DocumentReference addAttachedDocumentTarget() { 
      DocumentReference r = new DocumentReference();
      if (this.attachedDocumentTarget == null)
        this.attachedDocumentTarget = new ArrayList<DocumentReference>();
      this.attachedDocumentTarget.add(r);
      return r;
    }

    /**
     * @return {@link #masterFile} (A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).)
     */
    public List<Reference> getMasterFile() { 
      if (this.masterFile == null)
        this.masterFile = new ArrayList<Reference>();
      return this.masterFile;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setMasterFile(List<Reference> theMasterFile) { 
      this.masterFile = theMasterFile;
      return this;
    }

    public boolean hasMasterFile() { 
      if (this.masterFile == null)
        return false;
      for (Reference item : this.masterFile)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addMasterFile() { //3
      Reference t = new Reference();
      if (this.masterFile == null)
        this.masterFile = new ArrayList<Reference>();
      this.masterFile.add(t);
      return t;
    }

    public MedicinalProduct addMasterFile(Reference t) { //3
      if (t == null)
        return this;
      if (this.masterFile == null)
        this.masterFile = new ArrayList<Reference>();
      this.masterFile.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #masterFile}, creating it if it does not already exist
     */
    public Reference getMasterFileFirstRep() { 
      if (getMasterFile().isEmpty()) {
        addMasterFile();
      }
      return getMasterFile().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<DocumentReference> getMasterFileTarget() { 
      if (this.masterFileTarget == null)
        this.masterFileTarget = new ArrayList<DocumentReference>();
      return this.masterFileTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public DocumentReference addMasterFileTarget() { 
      DocumentReference r = new DocumentReference();
      if (this.masterFileTarget == null)
        this.masterFileTarget = new ArrayList<DocumentReference>();
      this.masterFileTarget.add(r);
      return r;
    }

    /**
     * @return {@link #name} (The product's name, including full name and possibly coded parts.)
     */
    public List<MedicinalProductNameComponent> getName() { 
      if (this.name == null)
        this.name = new ArrayList<MedicinalProductNameComponent>();
      return this.name;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setName(List<MedicinalProductNameComponent> theName) { 
      this.name = theName;
      return this;
    }

    public boolean hasName() { 
      if (this.name == null)
        return false;
      for (MedicinalProductNameComponent item : this.name)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductNameComponent addName() { //3
      MedicinalProductNameComponent t = new MedicinalProductNameComponent();
      if (this.name == null)
        this.name = new ArrayList<MedicinalProductNameComponent>();
      this.name.add(t);
      return t;
    }

    public MedicinalProduct addName(MedicinalProductNameComponent t) { //3
      if (t == null)
        return this;
      if (this.name == null)
        this.name = new ArrayList<MedicinalProductNameComponent>();
      this.name.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #name}, creating it if it does not already exist
     */
    public MedicinalProductNameComponent getNameFirstRep() { 
      if (getName().isEmpty()) {
        addName();
      }
      return getName().get(0);
    }

    /**
     * @return {@link #crossReference} (Reference to another product, e.g. for linking authorised to investigational product.)
     */
    public List<Identifier> getCrossReference() { 
      if (this.crossReference == null)
        this.crossReference = new ArrayList<Identifier>();
      return this.crossReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setCrossReference(List<Identifier> theCrossReference) { 
      this.crossReference = theCrossReference;
      return this;
    }

    public boolean hasCrossReference() { 
      if (this.crossReference == null)
        return false;
      for (Identifier item : this.crossReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addCrossReference() { //3
      Identifier t = new Identifier();
      if (this.crossReference == null)
        this.crossReference = new ArrayList<Identifier>();
      this.crossReference.add(t);
      return t;
    }

    public MedicinalProduct addCrossReference(Identifier t) { //3
      if (t == null)
        return this;
      if (this.crossReference == null)
        this.crossReference = new ArrayList<Identifier>();
      this.crossReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #crossReference}, creating it if it does not already exist
     */
    public Identifier getCrossReferenceFirstRep() { 
      if (getCrossReference().isEmpty()) {
        addCrossReference();
      }
      return getCrossReference().get(0);
    }

    /**
     * @return {@link #manufacturingBusinessOperation} (An operation applied to the product, for manufacturing or adminsitrative purpose.)
     */
    public List<MedicinalProductManufacturingBusinessOperationComponent> getManufacturingBusinessOperation() { 
      if (this.manufacturingBusinessOperation == null)
        this.manufacturingBusinessOperation = new ArrayList<MedicinalProductManufacturingBusinessOperationComponent>();
      return this.manufacturingBusinessOperation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProduct setManufacturingBusinessOperation(List<MedicinalProductManufacturingBusinessOperationComponent> theManufacturingBusinessOperation) { 
      this.manufacturingBusinessOperation = theManufacturingBusinessOperation;
      return this;
    }

    public boolean hasManufacturingBusinessOperation() { 
      if (this.manufacturingBusinessOperation == null)
        return false;
      for (MedicinalProductManufacturingBusinessOperationComponent item : this.manufacturingBusinessOperation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductManufacturingBusinessOperationComponent addManufacturingBusinessOperation() { //3
      MedicinalProductManufacturingBusinessOperationComponent t = new MedicinalProductManufacturingBusinessOperationComponent();
      if (this.manufacturingBusinessOperation == null)
        this.manufacturingBusinessOperation = new ArrayList<MedicinalProductManufacturingBusinessOperationComponent>();
      this.manufacturingBusinessOperation.add(t);
      return t;
    }

    public MedicinalProduct addManufacturingBusinessOperation(MedicinalProductManufacturingBusinessOperationComponent t) { //3
      if (t == null)
        return this;
      if (this.manufacturingBusinessOperation == null)
        this.manufacturingBusinessOperation = new ArrayList<MedicinalProductManufacturingBusinessOperationComponent>();
      this.manufacturingBusinessOperation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #manufacturingBusinessOperation}, creating it if it does not already exist
     */
    public MedicinalProductManufacturingBusinessOperationComponent getManufacturingBusinessOperationFirstRep() { 
      if (getManufacturingBusinessOperation().isEmpty()) {
        addManufacturingBusinessOperation();
      }
      return getManufacturingBusinessOperation().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business idenfifier for this product. Could be an MPID.", 0, 1, identifier));
        children.add(new Property("type", "CodeableConcept", "Regulatory type, e.g. Investigational or Authorized.", 0, 1, type));
        children.add(new Property("combinedPharmaceuticalDoseForm", "CodeableConcept", "The dose form for a single part product, or combined form of a multiple part product.", 0, 1, combinedPharmaceuticalDoseForm));
        children.add(new Property("additionalMonitoringIndicator", "CodeableConcept", "Whether the Medicinal Product is subject to additional monitoring for regulatory reasons.", 0, 1, additionalMonitoringIndicator));
        children.add(new Property("specialMeasures", "string", "Whether the Medicinal Product is subject to special measures for regulatory reasons.", 0, java.lang.Integer.MAX_VALUE, specialMeasures));
        children.add(new Property("paediatricUseIndicator", "CodeableConcept", "If authorised for use in children.", 0, 1, paediatricUseIndicator));
        children.add(new Property("orphanDesignationStatus", "CodeableConcept", "Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.", 0, 1, orphanDesignationStatus));
        children.add(new Property("productClassification", "CodeableConcept", "Allows the product to be classified by various systems.", 0, java.lang.Integer.MAX_VALUE, productClassification));
        children.add(new Property("marketingAuthorization", "Reference(MedicinalProductAuthorization)", "Product regulatory authorization.", 0, 1, marketingAuthorization));
        children.add(new Property("packagedMedicinalProduct", "Reference(MedicinalProductPackaged)", "Package representation for the product.", 0, java.lang.Integer.MAX_VALUE, packagedMedicinalProduct));
        children.add(new Property("pharmaceuticalProduct", "Reference(MedicinalProductPharmaceutical)", "Pharmaceutical aspects of product.", 0, java.lang.Integer.MAX_VALUE, pharmaceuticalProduct));
        children.add(new Property("clinicalParticulars", "Reference(MedicinalProductClinicals)", "Clinical particulars, indications etc.", 0, java.lang.Integer.MAX_VALUE, clinicalParticulars));
        children.add(new Property("attachedDocument", "Reference(DocumentReference)", "Supporting documentation, typically for regulatory submission.", 0, java.lang.Integer.MAX_VALUE, attachedDocument));
        children.add(new Property("masterFile", "Reference(DocumentReference)", "A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).", 0, java.lang.Integer.MAX_VALUE, masterFile));
        children.add(new Property("name", "", "The product's name, including full name and possibly coded parts.", 0, java.lang.Integer.MAX_VALUE, name));
        children.add(new Property("crossReference", "Identifier", "Reference to another product, e.g. for linking authorised to investigational product.", 0, java.lang.Integer.MAX_VALUE, crossReference));
        children.add(new Property("manufacturingBusinessOperation", "", "An operation applied to the product, for manufacturing or adminsitrative purpose.", 0, java.lang.Integer.MAX_VALUE, manufacturingBusinessOperation));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business idenfifier for this product. Could be an MPID.", 0, 1, identifier);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Regulatory type, e.g. Investigational or Authorized.", 0, 1, type);
        case -1992898487: /*combinedPharmaceuticalDoseForm*/  return new Property("combinedPharmaceuticalDoseForm", "CodeableConcept", "The dose form for a single part product, or combined form of a multiple part product.", 0, 1, combinedPharmaceuticalDoseForm);
        case 1935999744: /*additionalMonitoringIndicator*/  return new Property("additionalMonitoringIndicator", "CodeableConcept", "Whether the Medicinal Product is subject to additional monitoring for regulatory reasons.", 0, 1, additionalMonitoringIndicator);
        case 975102638: /*specialMeasures*/  return new Property("specialMeasures", "string", "Whether the Medicinal Product is subject to special measures for regulatory reasons.", 0, java.lang.Integer.MAX_VALUE, specialMeasures);
        case -1019867160: /*paediatricUseIndicator*/  return new Property("paediatricUseIndicator", "CodeableConcept", "If authorised for use in children.", 0, 1, paediatricUseIndicator);
        case 201000737: /*orphanDesignationStatus*/  return new Property("orphanDesignationStatus", "CodeableConcept", "Indicates if the medicinal product has an orphan designation for the treatment of a rare disease.", 0, 1, orphanDesignationStatus);
        case 1247936181: /*productClassification*/  return new Property("productClassification", "CodeableConcept", "Allows the product to be classified by various systems.", 0, java.lang.Integer.MAX_VALUE, productClassification);
        case 571831283: /*marketingAuthorization*/  return new Property("marketingAuthorization", "Reference(MedicinalProductAuthorization)", "Product regulatory authorization.", 0, 1, marketingAuthorization);
        case -361025513: /*packagedMedicinalProduct*/  return new Property("packagedMedicinalProduct", "Reference(MedicinalProductPackaged)", "Package representation for the product.", 0, java.lang.Integer.MAX_VALUE, packagedMedicinalProduct);
        case 443273260: /*pharmaceuticalProduct*/  return new Property("pharmaceuticalProduct", "Reference(MedicinalProductPharmaceutical)", "Pharmaceutical aspects of product.", 0, java.lang.Integer.MAX_VALUE, pharmaceuticalProduct);
        case -588103893: /*clinicalParticulars*/  return new Property("clinicalParticulars", "Reference(MedicinalProductClinicals)", "Clinical particulars, indications etc.", 0, java.lang.Integer.MAX_VALUE, clinicalParticulars);
        case -513945889: /*attachedDocument*/  return new Property("attachedDocument", "Reference(DocumentReference)", "Supporting documentation, typically for regulatory submission.", 0, java.lang.Integer.MAX_VALUE, attachedDocument);
        case -2039573762: /*masterFile*/  return new Property("masterFile", "Reference(DocumentReference)", "A master file for to the medicinal product (e.g. Pharmacovigilance System Master File).", 0, java.lang.Integer.MAX_VALUE, masterFile);
        case 3373707: /*name*/  return new Property("name", "", "The product's name, including full name and possibly coded parts.", 0, java.lang.Integer.MAX_VALUE, name);
        case -986968341: /*crossReference*/  return new Property("crossReference", "Identifier", "Reference to another product, e.g. for linking authorised to investigational product.", 0, java.lang.Integer.MAX_VALUE, crossReference);
        case -171103255: /*manufacturingBusinessOperation*/  return new Property("manufacturingBusinessOperation", "", "An operation applied to the product, for manufacturing or adminsitrative purpose.", 0, java.lang.Integer.MAX_VALUE, manufacturingBusinessOperation);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1992898487: /*combinedPharmaceuticalDoseForm*/ return this.combinedPharmaceuticalDoseForm == null ? new Base[0] : new Base[] {this.combinedPharmaceuticalDoseForm}; // CodeableConcept
        case 1935999744: /*additionalMonitoringIndicator*/ return this.additionalMonitoringIndicator == null ? new Base[0] : new Base[] {this.additionalMonitoringIndicator}; // CodeableConcept
        case 975102638: /*specialMeasures*/ return this.specialMeasures == null ? new Base[0] : this.specialMeasures.toArray(new Base[this.specialMeasures.size()]); // StringType
        case -1019867160: /*paediatricUseIndicator*/ return this.paediatricUseIndicator == null ? new Base[0] : new Base[] {this.paediatricUseIndicator}; // CodeableConcept
        case 201000737: /*orphanDesignationStatus*/ return this.orphanDesignationStatus == null ? new Base[0] : new Base[] {this.orphanDesignationStatus}; // CodeableConcept
        case 1247936181: /*productClassification*/ return this.productClassification == null ? new Base[0] : this.productClassification.toArray(new Base[this.productClassification.size()]); // CodeableConcept
        case 571831283: /*marketingAuthorization*/ return this.marketingAuthorization == null ? new Base[0] : new Base[] {this.marketingAuthorization}; // Reference
        case -361025513: /*packagedMedicinalProduct*/ return this.packagedMedicinalProduct == null ? new Base[0] : this.packagedMedicinalProduct.toArray(new Base[this.packagedMedicinalProduct.size()]); // Reference
        case 443273260: /*pharmaceuticalProduct*/ return this.pharmaceuticalProduct == null ? new Base[0] : this.pharmaceuticalProduct.toArray(new Base[this.pharmaceuticalProduct.size()]); // Reference
        case -588103893: /*clinicalParticulars*/ return this.clinicalParticulars == null ? new Base[0] : this.clinicalParticulars.toArray(new Base[this.clinicalParticulars.size()]); // Reference
        case -513945889: /*attachedDocument*/ return this.attachedDocument == null ? new Base[0] : this.attachedDocument.toArray(new Base[this.attachedDocument.size()]); // Reference
        case -2039573762: /*masterFile*/ return this.masterFile == null ? new Base[0] : this.masterFile.toArray(new Base[this.masterFile.size()]); // Reference
        case 3373707: /*name*/ return this.name == null ? new Base[0] : this.name.toArray(new Base[this.name.size()]); // MedicinalProductNameComponent
        case -986968341: /*crossReference*/ return this.crossReference == null ? new Base[0] : this.crossReference.toArray(new Base[this.crossReference.size()]); // Identifier
        case -171103255: /*manufacturingBusinessOperation*/ return this.manufacturingBusinessOperation == null ? new Base[0] : this.manufacturingBusinessOperation.toArray(new Base[this.manufacturingBusinessOperation.size()]); // MedicinalProductManufacturingBusinessOperationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1992898487: // combinedPharmaceuticalDoseForm
          this.combinedPharmaceuticalDoseForm = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1935999744: // additionalMonitoringIndicator
          this.additionalMonitoringIndicator = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 975102638: // specialMeasures
          this.getSpecialMeasures().add(castToString(value)); // StringType
          return value;
        case -1019867160: // paediatricUseIndicator
          this.paediatricUseIndicator = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 201000737: // orphanDesignationStatus
          this.orphanDesignationStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1247936181: // productClassification
          this.getProductClassification().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 571831283: // marketingAuthorization
          this.marketingAuthorization = castToReference(value); // Reference
          return value;
        case -361025513: // packagedMedicinalProduct
          this.getPackagedMedicinalProduct().add(castToReference(value)); // Reference
          return value;
        case 443273260: // pharmaceuticalProduct
          this.getPharmaceuticalProduct().add(castToReference(value)); // Reference
          return value;
        case -588103893: // clinicalParticulars
          this.getClinicalParticulars().add(castToReference(value)); // Reference
          return value;
        case -513945889: // attachedDocument
          this.getAttachedDocument().add(castToReference(value)); // Reference
          return value;
        case -2039573762: // masterFile
          this.getMasterFile().add(castToReference(value)); // Reference
          return value;
        case 3373707: // name
          this.getName().add((MedicinalProductNameComponent) value); // MedicinalProductNameComponent
          return value;
        case -986968341: // crossReference
          this.getCrossReference().add(castToIdentifier(value)); // Identifier
          return value;
        case -171103255: // manufacturingBusinessOperation
          this.getManufacturingBusinessOperation().add((MedicinalProductManufacturingBusinessOperationComponent) value); // MedicinalProductManufacturingBusinessOperationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("combinedPharmaceuticalDoseForm")) {
          this.combinedPharmaceuticalDoseForm = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("additionalMonitoringIndicator")) {
          this.additionalMonitoringIndicator = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("specialMeasures")) {
          this.getSpecialMeasures().add(castToString(value));
        } else if (name.equals("paediatricUseIndicator")) {
          this.paediatricUseIndicator = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("orphanDesignationStatus")) {
          this.orphanDesignationStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("productClassification")) {
          this.getProductClassification().add(castToCodeableConcept(value));
        } else if (name.equals("marketingAuthorization")) {
          this.marketingAuthorization = castToReference(value); // Reference
        } else if (name.equals("packagedMedicinalProduct")) {
          this.getPackagedMedicinalProduct().add(castToReference(value));
        } else if (name.equals("pharmaceuticalProduct")) {
          this.getPharmaceuticalProduct().add(castToReference(value));
        } else if (name.equals("clinicalParticulars")) {
          this.getClinicalParticulars().add(castToReference(value));
        } else if (name.equals("attachedDocument")) {
          this.getAttachedDocument().add(castToReference(value));
        } else if (name.equals("masterFile")) {
          this.getMasterFile().add(castToReference(value));
        } else if (name.equals("name")) {
          this.getName().add((MedicinalProductNameComponent) value);
        } else if (name.equals("crossReference")) {
          this.getCrossReference().add(castToIdentifier(value));
        } else if (name.equals("manufacturingBusinessOperation")) {
          this.getManufacturingBusinessOperation().add((MedicinalProductManufacturingBusinessOperationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3575610:  return getType(); 
        case -1992898487:  return getCombinedPharmaceuticalDoseForm(); 
        case 1935999744:  return getAdditionalMonitoringIndicator(); 
        case 975102638:  return addSpecialMeasuresElement();
        case -1019867160:  return getPaediatricUseIndicator(); 
        case 201000737:  return getOrphanDesignationStatus(); 
        case 1247936181:  return addProductClassification(); 
        case 571831283:  return getMarketingAuthorization(); 
        case -361025513:  return addPackagedMedicinalProduct(); 
        case 443273260:  return addPharmaceuticalProduct(); 
        case -588103893:  return addClinicalParticulars(); 
        case -513945889:  return addAttachedDocument(); 
        case -2039573762:  return addMasterFile(); 
        case 3373707:  return addName(); 
        case -986968341:  return addCrossReference(); 
        case -171103255:  return addManufacturingBusinessOperation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1992898487: /*combinedPharmaceuticalDoseForm*/ return new String[] {"CodeableConcept"};
        case 1935999744: /*additionalMonitoringIndicator*/ return new String[] {"CodeableConcept"};
        case 975102638: /*specialMeasures*/ return new String[] {"string"};
        case -1019867160: /*paediatricUseIndicator*/ return new String[] {"CodeableConcept"};
        case 201000737: /*orphanDesignationStatus*/ return new String[] {"CodeableConcept"};
        case 1247936181: /*productClassification*/ return new String[] {"CodeableConcept"};
        case 571831283: /*marketingAuthorization*/ return new String[] {"Reference"};
        case -361025513: /*packagedMedicinalProduct*/ return new String[] {"Reference"};
        case 443273260: /*pharmaceuticalProduct*/ return new String[] {"Reference"};
        case -588103893: /*clinicalParticulars*/ return new String[] {"Reference"};
        case -513945889: /*attachedDocument*/ return new String[] {"Reference"};
        case -2039573762: /*masterFile*/ return new String[] {"Reference"};
        case 3373707: /*name*/ return new String[] {};
        case -986968341: /*crossReference*/ return new String[] {"Identifier"};
        case -171103255: /*manufacturingBusinessOperation*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("combinedPharmaceuticalDoseForm")) {
          this.combinedPharmaceuticalDoseForm = new CodeableConcept();
          return this.combinedPharmaceuticalDoseForm;
        }
        else if (name.equals("additionalMonitoringIndicator")) {
          this.additionalMonitoringIndicator = new CodeableConcept();
          return this.additionalMonitoringIndicator;
        }
        else if (name.equals("specialMeasures")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProduct.specialMeasures");
        }
        else if (name.equals("paediatricUseIndicator")) {
          this.paediatricUseIndicator = new CodeableConcept();
          return this.paediatricUseIndicator;
        }
        else if (name.equals("orphanDesignationStatus")) {
          this.orphanDesignationStatus = new CodeableConcept();
          return this.orphanDesignationStatus;
        }
        else if (name.equals("productClassification")) {
          return addProductClassification();
        }
        else if (name.equals("marketingAuthorization")) {
          this.marketingAuthorization = new Reference();
          return this.marketingAuthorization;
        }
        else if (name.equals("packagedMedicinalProduct")) {
          return addPackagedMedicinalProduct();
        }
        else if (name.equals("pharmaceuticalProduct")) {
          return addPharmaceuticalProduct();
        }
        else if (name.equals("clinicalParticulars")) {
          return addClinicalParticulars();
        }
        else if (name.equals("attachedDocument")) {
          return addAttachedDocument();
        }
        else if (name.equals("masterFile")) {
          return addMasterFile();
        }
        else if (name.equals("name")) {
          return addName();
        }
        else if (name.equals("crossReference")) {
          return addCrossReference();
        }
        else if (name.equals("manufacturingBusinessOperation")) {
          return addManufacturingBusinessOperation();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProduct";

  }

      public MedicinalProduct copy() {
        MedicinalProduct dst = new MedicinalProduct();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.combinedPharmaceuticalDoseForm = combinedPharmaceuticalDoseForm == null ? null : combinedPharmaceuticalDoseForm.copy();
        dst.additionalMonitoringIndicator = additionalMonitoringIndicator == null ? null : additionalMonitoringIndicator.copy();
        if (specialMeasures != null) {
          dst.specialMeasures = new ArrayList<StringType>();
          for (StringType i : specialMeasures)
            dst.specialMeasures.add(i.copy());
        };
        dst.paediatricUseIndicator = paediatricUseIndicator == null ? null : paediatricUseIndicator.copy();
        dst.orphanDesignationStatus = orphanDesignationStatus == null ? null : orphanDesignationStatus.copy();
        if (productClassification != null) {
          dst.productClassification = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : productClassification)
            dst.productClassification.add(i.copy());
        };
        dst.marketingAuthorization = marketingAuthorization == null ? null : marketingAuthorization.copy();
        if (packagedMedicinalProduct != null) {
          dst.packagedMedicinalProduct = new ArrayList<Reference>();
          for (Reference i : packagedMedicinalProduct)
            dst.packagedMedicinalProduct.add(i.copy());
        };
        if (pharmaceuticalProduct != null) {
          dst.pharmaceuticalProduct = new ArrayList<Reference>();
          for (Reference i : pharmaceuticalProduct)
            dst.pharmaceuticalProduct.add(i.copy());
        };
        if (clinicalParticulars != null) {
          dst.clinicalParticulars = new ArrayList<Reference>();
          for (Reference i : clinicalParticulars)
            dst.clinicalParticulars.add(i.copy());
        };
        if (attachedDocument != null) {
          dst.attachedDocument = new ArrayList<Reference>();
          for (Reference i : attachedDocument)
            dst.attachedDocument.add(i.copy());
        };
        if (masterFile != null) {
          dst.masterFile = new ArrayList<Reference>();
          for (Reference i : masterFile)
            dst.masterFile.add(i.copy());
        };
        if (name != null) {
          dst.name = new ArrayList<MedicinalProductNameComponent>();
          for (MedicinalProductNameComponent i : name)
            dst.name.add(i.copy());
        };
        if (crossReference != null) {
          dst.crossReference = new ArrayList<Identifier>();
          for (Identifier i : crossReference)
            dst.crossReference.add(i.copy());
        };
        if (manufacturingBusinessOperation != null) {
          dst.manufacturingBusinessOperation = new ArrayList<MedicinalProductManufacturingBusinessOperationComponent>();
          for (MedicinalProductManufacturingBusinessOperationComponent i : manufacturingBusinessOperation)
            dst.manufacturingBusinessOperation.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProduct typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProduct))
          return false;
        MedicinalProduct o = (MedicinalProduct) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(combinedPharmaceuticalDoseForm, o.combinedPharmaceuticalDoseForm, true)
           && compareDeep(additionalMonitoringIndicator, o.additionalMonitoringIndicator, true) && compareDeep(specialMeasures, o.specialMeasures, true)
           && compareDeep(paediatricUseIndicator, o.paediatricUseIndicator, true) && compareDeep(orphanDesignationStatus, o.orphanDesignationStatus, true)
           && compareDeep(productClassification, o.productClassification, true) && compareDeep(marketingAuthorization, o.marketingAuthorization, true)
           && compareDeep(packagedMedicinalProduct, o.packagedMedicinalProduct, true) && compareDeep(pharmaceuticalProduct, o.pharmaceuticalProduct, true)
           && compareDeep(clinicalParticulars, o.clinicalParticulars, true) && compareDeep(attachedDocument, o.attachedDocument, true)
           && compareDeep(masterFile, o.masterFile, true) && compareDeep(name, o.name, true) && compareDeep(crossReference, o.crossReference, true)
           && compareDeep(manufacturingBusinessOperation, o.manufacturingBusinessOperation, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProduct))
          return false;
        MedicinalProduct o = (MedicinalProduct) other_;
        return compareValues(specialMeasures, o.specialMeasures, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, combinedPharmaceuticalDoseForm
          , additionalMonitoringIndicator, specialMeasures, paediatricUseIndicator, orphanDesignationStatus
          , productClassification, marketingAuthorization, packagedMedicinalProduct, pharmaceuticalProduct
          , clinicalParticulars, attachedDocument, masterFile, name, crossReference, manufacturingBusinessOperation
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProduct;
   }


}

