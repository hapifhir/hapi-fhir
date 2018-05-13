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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The regulatory authorization of a medicinal product.
 */
@ResourceDef(name="MedicinalProductAuthorization", profile="http://hl7.org/fhir/Profile/MedicinalProductAuthorization")
public class MedicinalProductAuthorization extends DomainResource {

    @Block()
    public static class MedicinalProductAuthorizationJurisdictionalAuthorizationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Country of authorization.
         */
        @Child(name = "country", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Country of authorization", formalDefinition="Country of authorization." )
        protected CodeableConcept country;

        /**
         * Jurisdiction within a country.
         */
        @Child(name = "jurisdiction", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Jurisdiction within a country", formalDefinition="Jurisdiction within a country." )
        protected CodeableConcept jurisdiction;

        /**
         * The assigned number for the marketing authorization.
         */
        @Child(name = "number", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The assigned number for the marketing authorization", formalDefinition="The assigned number for the marketing authorization." )
        protected Identifier number;

        /**
         * The legal status of supply in a jurisdiction or region.
         */
        @Child(name = "legalStatusOfSupply", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The legal status of supply in a jurisdiction or region", formalDefinition="The legal status of supply in a jurisdiction or region." )
        protected CodeableConcept legalStatusOfSupply;

        private static final long serialVersionUID = 271408949L;

    /**
     * Constructor
     */
      public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent(CodeableConcept country) {
        super();
        this.country = country;
      }

        /**
         * @return {@link #country} (Country of authorization.)
         */
        public CodeableConcept getCountry() { 
          if (this.country == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationJurisdictionalAuthorizationComponent.country");
            else if (Configuration.doAutoCreate())
              this.country = new CodeableConcept(); // cc
          return this.country;
        }

        public boolean hasCountry() { 
          return this.country != null && !this.country.isEmpty();
        }

        /**
         * @param value {@link #country} (Country of authorization.)
         */
        public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent setCountry(CodeableConcept value) { 
          this.country = value;
          return this;
        }

        /**
         * @return {@link #jurisdiction} (Jurisdiction within a country.)
         */
        public CodeableConcept getJurisdiction() { 
          if (this.jurisdiction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationJurisdictionalAuthorizationComponent.jurisdiction");
            else if (Configuration.doAutoCreate())
              this.jurisdiction = new CodeableConcept(); // cc
          return this.jurisdiction;
        }

        public boolean hasJurisdiction() { 
          return this.jurisdiction != null && !this.jurisdiction.isEmpty();
        }

        /**
         * @param value {@link #jurisdiction} (Jurisdiction within a country.)
         */
        public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent setJurisdiction(CodeableConcept value) { 
          this.jurisdiction = value;
          return this;
        }

        /**
         * @return {@link #number} (The assigned number for the marketing authorization.)
         */
        public Identifier getNumber() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationJurisdictionalAuthorizationComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new Identifier(); // cc
          return this.number;
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (The assigned number for the marketing authorization.)
         */
        public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent setNumber(Identifier value) { 
          this.number = value;
          return this;
        }

        /**
         * @return {@link #legalStatusOfSupply} (The legal status of supply in a jurisdiction or region.)
         */
        public CodeableConcept getLegalStatusOfSupply() { 
          if (this.legalStatusOfSupply == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationJurisdictionalAuthorizationComponent.legalStatusOfSupply");
            else if (Configuration.doAutoCreate())
              this.legalStatusOfSupply = new CodeableConcept(); // cc
          return this.legalStatusOfSupply;
        }

        public boolean hasLegalStatusOfSupply() { 
          return this.legalStatusOfSupply != null && !this.legalStatusOfSupply.isEmpty();
        }

        /**
         * @param value {@link #legalStatusOfSupply} (The legal status of supply in a jurisdiction or region.)
         */
        public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent setLegalStatusOfSupply(CodeableConcept value) { 
          this.legalStatusOfSupply = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("country", "CodeableConcept", "Country of authorization.", 0, 1, country));
          children.add(new Property("jurisdiction", "CodeableConcept", "Jurisdiction within a country.", 0, 1, jurisdiction));
          children.add(new Property("number", "Identifier", "The assigned number for the marketing authorization.", 0, 1, number));
          children.add(new Property("legalStatusOfSupply", "CodeableConcept", "The legal status of supply in a jurisdiction or region.", 0, 1, legalStatusOfSupply));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 957831062: /*country*/  return new Property("country", "CodeableConcept", "Country of authorization.", 0, 1, country);
          case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "Jurisdiction within a country.", 0, 1, jurisdiction);
          case -1034364087: /*number*/  return new Property("number", "Identifier", "The assigned number for the marketing authorization.", 0, 1, number);
          case -844874031: /*legalStatusOfSupply*/  return new Property("legalStatusOfSupply", "CodeableConcept", "The legal status of supply in a jurisdiction or region.", 0, 1, legalStatusOfSupply);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 957831062: /*country*/ return this.country == null ? new Base[0] : new Base[] {this.country}; // CodeableConcept
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : new Base[] {this.jurisdiction}; // CodeableConcept
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // Identifier
        case -844874031: /*legalStatusOfSupply*/ return this.legalStatusOfSupply == null ? new Base[0] : new Base[] {this.legalStatusOfSupply}; // CodeableConcept
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
        case -1034364087: // number
          this.number = castToIdentifier(value); // Identifier
          return value;
        case -844874031: // legalStatusOfSupply
          this.legalStatusOfSupply = castToCodeableConcept(value); // CodeableConcept
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
        } else if (name.equals("number")) {
          this.number = castToIdentifier(value); // Identifier
        } else if (name.equals("legalStatusOfSupply")) {
          this.legalStatusOfSupply = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 957831062:  return getCountry(); 
        case -507075711:  return getJurisdiction(); 
        case -1034364087:  return getNumber(); 
        case -844874031:  return getLegalStatusOfSupply(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 957831062: /*country*/ return new String[] {"CodeableConcept"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -1034364087: /*number*/ return new String[] {"Identifier"};
        case -844874031: /*legalStatusOfSupply*/ return new String[] {"CodeableConcept"};
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
        else if (name.equals("number")) {
          this.number = new Identifier();
          return this.number;
        }
        else if (name.equals("legalStatusOfSupply")) {
          this.legalStatusOfSupply = new CodeableConcept();
          return this.legalStatusOfSupply;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent copy() {
        MedicinalProductAuthorizationJurisdictionalAuthorizationComponent dst = new MedicinalProductAuthorizationJurisdictionalAuthorizationComponent();
        copyValues(dst);
        dst.country = country == null ? null : country.copy();
        dst.jurisdiction = jurisdiction == null ? null : jurisdiction.copy();
        dst.number = number == null ? null : number.copy();
        dst.legalStatusOfSupply = legalStatusOfSupply == null ? null : legalStatusOfSupply.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorizationJurisdictionalAuthorizationComponent))
          return false;
        MedicinalProductAuthorizationJurisdictionalAuthorizationComponent o = (MedicinalProductAuthorizationJurisdictionalAuthorizationComponent) other_;
        return compareDeep(country, o.country, true) && compareDeep(jurisdiction, o.jurisdiction, true)
           && compareDeep(number, o.number, true) && compareDeep(legalStatusOfSupply, o.legalStatusOfSupply, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorizationJurisdictionalAuthorizationComponent))
          return false;
        MedicinalProductAuthorizationJurisdictionalAuthorizationComponent o = (MedicinalProductAuthorizationJurisdictionalAuthorizationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(country, jurisdiction, number
          , legalStatusOfSupply);
      }

  public String fhirType() {
    return "MedicinalProductAuthorization.jurisdictionalAuthorization";

  }

  }

    @Block()
    public static class MedicinalProductAuthorizationProcedureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifier for this procedure.
         */
        @Child(name = "number", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier for this procedure", formalDefinition="Identifier for this procedure." )
        protected Identifier number;

        /**
         * Type of procedure.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of procedure", formalDefinition="Type of procedure." )
        protected CodeableConcept type;

        /**
         * Date of procedure.
         */
        @Child(name = "date", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date of procedure", formalDefinition="Date of procedure." )
        protected Period date;

        /**
         * Applcations submitted to obtain a marketing authorization.
         */
        @Child(name = "application", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Applcations submitted to obtain a marketing authorization", formalDefinition="Applcations submitted to obtain a marketing authorization." )
        protected List<MedicinalProductAuthorizationProcedureApplicationComponent> application;

        private static final long serialVersionUID = 1087335734L;

    /**
     * Constructor
     */
      public MedicinalProductAuthorizationProcedureComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductAuthorizationProcedureComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #number} (Identifier for this procedure.)
         */
        public Identifier getNumber() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationProcedureComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new Identifier(); // cc
          return this.number;
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (Identifier for this procedure.)
         */
        public MedicinalProductAuthorizationProcedureComponent setNumber(Identifier value) { 
          this.number = value;
          return this;
        }

        /**
         * @return {@link #type} (Type of procedure.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationProcedureComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of procedure.)
         */
        public MedicinalProductAuthorizationProcedureComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #date} (Date of procedure.)
         */
        public Period getDate() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationProcedureComponent.date");
            else if (Configuration.doAutoCreate())
              this.date = new Period(); // cc
          return this.date;
        }

        public boolean hasDate() { 
          return this.date != null && !this.date.isEmpty();
        }

        /**
         * @param value {@link #date} (Date of procedure.)
         */
        public MedicinalProductAuthorizationProcedureComponent setDate(Period value) { 
          this.date = value;
          return this;
        }

        /**
         * @return {@link #application} (Applcations submitted to obtain a marketing authorization.)
         */
        public List<MedicinalProductAuthorizationProcedureApplicationComponent> getApplication() { 
          if (this.application == null)
            this.application = new ArrayList<MedicinalProductAuthorizationProcedureApplicationComponent>();
          return this.application;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductAuthorizationProcedureComponent setApplication(List<MedicinalProductAuthorizationProcedureApplicationComponent> theApplication) { 
          this.application = theApplication;
          return this;
        }

        public boolean hasApplication() { 
          if (this.application == null)
            return false;
          for (MedicinalProductAuthorizationProcedureApplicationComponent item : this.application)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductAuthorizationProcedureApplicationComponent addApplication() { //3
          MedicinalProductAuthorizationProcedureApplicationComponent t = new MedicinalProductAuthorizationProcedureApplicationComponent();
          if (this.application == null)
            this.application = new ArrayList<MedicinalProductAuthorizationProcedureApplicationComponent>();
          this.application.add(t);
          return t;
        }

        public MedicinalProductAuthorizationProcedureComponent addApplication(MedicinalProductAuthorizationProcedureApplicationComponent t) { //3
          if (t == null)
            return this;
          if (this.application == null)
            this.application = new ArrayList<MedicinalProductAuthorizationProcedureApplicationComponent>();
          this.application.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #application}, creating it if it does not already exist
         */
        public MedicinalProductAuthorizationProcedureApplicationComponent getApplicationFirstRep() { 
          if (getApplication().isEmpty()) {
            addApplication();
          }
          return getApplication().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("number", "Identifier", "Identifier for this procedure.", 0, 1, number));
          children.add(new Property("type", "CodeableConcept", "Type of procedure.", 0, 1, type));
          children.add(new Property("date", "Period", "Date of procedure.", 0, 1, date));
          children.add(new Property("application", "", "Applcations submitted to obtain a marketing authorization.", 0, java.lang.Integer.MAX_VALUE, application));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1034364087: /*number*/  return new Property("number", "Identifier", "Identifier for this procedure.", 0, 1, number);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of procedure.", 0, 1, type);
          case 3076014: /*date*/  return new Property("date", "Period", "Date of procedure.", 0, 1, date);
          case 1554253136: /*application*/  return new Property("application", "", "Applcations submitted to obtain a marketing authorization.", 0, java.lang.Integer.MAX_VALUE, application);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // Period
        case 1554253136: /*application*/ return this.application == null ? new Base[0] : this.application.toArray(new Base[this.application.size()]); // MedicinalProductAuthorizationProcedureApplicationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.number = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToPeriod(value); // Period
          return value;
        case 1554253136: // application
          this.getApplication().add((MedicinalProductAuthorizationProcedureApplicationComponent) value); // MedicinalProductAuthorizationProcedureApplicationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number")) {
          this.number = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("date")) {
          this.date = castToPeriod(value); // Period
        } else if (name.equals("application")) {
          this.getApplication().add((MedicinalProductAuthorizationProcedureApplicationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087:  return getNumber(); 
        case 3575610:  return getType(); 
        case 3076014:  return getDate(); 
        case 1554253136:  return addApplication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"Period"};
        case 1554253136: /*application*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          this.number = new Identifier();
          return this.number;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("date")) {
          this.date = new Period();
          return this.date;
        }
        else if (name.equals("application")) {
          return addApplication();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductAuthorizationProcedureComponent copy() {
        MedicinalProductAuthorizationProcedureComponent dst = new MedicinalProductAuthorizationProcedureComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.type = type == null ? null : type.copy();
        dst.date = date == null ? null : date.copy();
        if (application != null) {
          dst.application = new ArrayList<MedicinalProductAuthorizationProcedureApplicationComponent>();
          for (MedicinalProductAuthorizationProcedureApplicationComponent i : application)
            dst.application.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorizationProcedureComponent))
          return false;
        MedicinalProductAuthorizationProcedureComponent o = (MedicinalProductAuthorizationProcedureComponent) other_;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(date, o.date, true)
           && compareDeep(application, o.application, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorizationProcedureComponent))
          return false;
        MedicinalProductAuthorizationProcedureComponent o = (MedicinalProductAuthorizationProcedureComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(number, type, date, application
          );
      }

  public String fhirType() {
    return "MedicinalProductAuthorization.procedure";

  }

  }

    @Block()
    public static class MedicinalProductAuthorizationProcedureApplicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A unique identifier for the specific instance of an application shall be provided in text. The application identifier/number is usually assigned by a Medicines Regulatory Agency.
         */
        @Child(name = "number", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A unique identifier for the specific instance of an application shall be provided in text. The application identifier/number is usually assigned by a Medicines Regulatory Agency", formalDefinition="A unique identifier for the specific instance of an application shall be provided in text. The application identifier/number is usually assigned by a Medicines Regulatory Agency." )
        protected Identifier number;

        /**
         * The type of the application.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of the application", formalDefinition="The type of the application." )
        protected CodeableConcept type;

        /**
         * Date that the application was made.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date that the application was made", formalDefinition="Date that the application was made." )
        protected DateTimeType date;

        private static final long serialVersionUID = -1806825708L;

    /**
     * Constructor
     */
      public MedicinalProductAuthorizationProcedureApplicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductAuthorizationProcedureApplicationComponent(Identifier number, CodeableConcept type, DateTimeType date) {
        super();
        this.number = number;
        this.type = type;
        this.date = date;
      }

        /**
         * @return {@link #number} (A unique identifier for the specific instance of an application shall be provided in text. The application identifier/number is usually assigned by a Medicines Regulatory Agency.)
         */
        public Identifier getNumber() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationProcedureApplicationComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new Identifier(); // cc
          return this.number;
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (A unique identifier for the specific instance of an application shall be provided in text. The application identifier/number is usually assigned by a Medicines Regulatory Agency.)
         */
        public MedicinalProductAuthorizationProcedureApplicationComponent setNumber(Identifier value) { 
          this.number = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of the application.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationProcedureApplicationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the application.)
         */
        public MedicinalProductAuthorizationProcedureApplicationComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #date} (Date that the application was made.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductAuthorizationProcedureApplicationComponent.date");
            else if (Configuration.doAutoCreate())
              this.date = new DateTimeType(); // bb
          return this.date;
        }

        public boolean hasDateElement() { 
          return this.date != null && !this.date.isEmpty();
        }

        public boolean hasDate() { 
          return this.date != null && !this.date.isEmpty();
        }

        /**
         * @param value {@link #date} (Date that the application was made.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public MedicinalProductAuthorizationProcedureApplicationComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Date that the application was made.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Date that the application was made.
         */
        public MedicinalProductAuthorizationProcedureApplicationComponent setDate(Date value) { 
            if (this.date == null)
              this.date = new DateTimeType();
            this.date.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("number", "Identifier", "A unique identifier for the specific instance of an application shall be provided in text. The application identifier/number is usually assigned by a Medicines Regulatory Agency.", 0, 1, number));
          children.add(new Property("type", "CodeableConcept", "The type of the application.", 0, 1, type));
          children.add(new Property("date", "dateTime", "Date that the application was made.", 0, 1, date));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1034364087: /*number*/  return new Property("number", "Identifier", "A unique identifier for the specific instance of an application shall be provided in text. The application identifier/number is usually assigned by a Medicines Regulatory Agency.", 0, 1, number);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of the application.", 0, 1, type);
          case 3076014: /*date*/  return new Property("date", "dateTime", "Date that the application was made.", 0, 1, date);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : new Base[] {this.number}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.number = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number")) {
          this.number = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087:  return getNumber(); 
        case 3575610:  return getType(); 
        case 3076014:  return getDateElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          this.number = new Identifier();
          return this.number;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductAuthorization.date");
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductAuthorizationProcedureApplicationComponent copy() {
        MedicinalProductAuthorizationProcedureApplicationComponent dst = new MedicinalProductAuthorizationProcedureApplicationComponent();
        copyValues(dst);
        dst.number = number == null ? null : number.copy();
        dst.type = type == null ? null : type.copy();
        dst.date = date == null ? null : date.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorizationProcedureApplicationComponent))
          return false;
        MedicinalProductAuthorizationProcedureApplicationComponent o = (MedicinalProductAuthorizationProcedureApplicationComponent) other_;
        return compareDeep(number, o.number, true) && compareDeep(type, o.type, true) && compareDeep(date, o.date, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorizationProcedureApplicationComponent))
          return false;
        MedicinalProductAuthorizationProcedureApplicationComponent o = (MedicinalProductAuthorizationProcedureApplicationComponent) other_;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(number, type, date);
      }

  public String fhirType() {
    return "MedicinalProductAuthorization.procedure.application";

  }

  }

    /**
     * Business identifier for the marketing authorization, as assigned by a regulator.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier for the marketing authorization, as assigned by a regulator", formalDefinition="Business identifier for the marketing authorization, as assigned by a regulator." )
    protected Identifier identifier;

    /**
     * The country in which the marketing authorization has been granted.
     */
    @Child(name = "country", type = {CodeableConcept.class}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The country in which the marketing authorization has been granted", formalDefinition="The country in which the marketing authorization has been granted." )
    protected List<CodeableConcept> country;

    /**
     * The legal status of supply of the medicinal product as classified by the regulator.
     */
    @Child(name = "legalStatusOfSupply", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The legal status of supply of the medicinal product as classified by the regulator", formalDefinition="The legal status of supply of the medicinal product as classified by the regulator." )
    protected CodeableConcept legalStatusOfSupply;

    /**
     * The status of the marketing authorization.
     */
    @Child(name = "status", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The status of the marketing authorization", formalDefinition="The status of the marketing authorization." )
    protected CodeableConcept status;

    /**
     * The date at which the given status has become applicable.
     */
    @Child(name = "statusDate", type = {DateTimeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date at which the given status has become applicable", formalDefinition="The date at which the given status has become applicable." )
    protected DateTimeType statusDate;

    /**
     * The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.
     */
    @Child(name = "restoreDate", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored", formalDefinition="The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored." )
    protected DateTimeType restoreDate;

    /**
     * The beginning of the time period in which the marketing authorization is in the specific status shall be specified A complete date consisting of day, month and year shall be specified using the ISO 8601 date format.
     */
    @Child(name = "validityPeriod", type = {Period.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The beginning of the time period in which the marketing authorization is in the specific status shall be specified A complete date consisting of day, month and year shall be specified using the ISO 8601 date format", formalDefinition="The beginning of the time period in which the marketing authorization is in the specific status shall be specified A complete date consisting of day, month and year shall be specified using the ISO 8601 date format." )
    protected Period validityPeriod;

    /**
     * A period of time after authorization before generic product applicatiosn can be submitted.
     */
    @Child(name = "dataExclusivityPeriod", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A period of time after authorization before generic product applicatiosn can be submitted", formalDefinition="A period of time after authorization before generic product applicatiosn can be submitted." )
    protected Period dataExclusivityPeriod;

    /**
     * The date when the first authorization was granted by a Medicines Regulatory Agency.
     */
    @Child(name = "dateOfFirstAuthorization", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date when the first authorization was granted by a Medicines Regulatory Agency", formalDefinition="The date when the first authorization was granted by a Medicines Regulatory Agency." )
    protected DateTimeType dateOfFirstAuthorization;

    /**
     * Date of first marketing authorization for a company's new medicinal product in any country in the World.
     */
    @Child(name = "internationalBirthDate", type = {DateTimeType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date of first marketing authorization for a company's new medicinal product in any country in the World", formalDefinition="Date of first marketing authorization for a company's new medicinal product in any country in the World." )
    protected DateTimeType internationalBirthDate;

    /**
     * Authorization in areas within a country.
     */
    @Child(name = "jurisdictionalAuthorization", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Authorization in areas within a country", formalDefinition="Authorization in areas within a country." )
    protected List<MedicinalProductAuthorizationJurisdictionalAuthorizationComponent> jurisdictionalAuthorization;

    /**
     * Marketing Authorization Holder.
     */
    @Child(name = "holder", type = {Organization.class}, order=11, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Marketing Authorization Holder", formalDefinition="Marketing Authorization Holder." )
    protected Reference holder;

    /**
     * The actual object that is the target of the reference (Marketing Authorization Holder.)
     */
    protected Organization holderTarget;

    /**
     * Medicines Regulatory Agency.
     */
    @Child(name = "regulator", type = {Organization.class}, order=12, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Medicines Regulatory Agency", formalDefinition="Medicines Regulatory Agency." )
    protected Reference regulator;

    /**
     * The actual object that is the target of the reference (Medicines Regulatory Agency.)
     */
    protected Organization regulatorTarget;

    /**
     * The regulatory procedure for granting or amending a marketing authorization.
     */
    @Child(name = "procedure", type = {}, order=13, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The regulatory procedure for granting or amending a marketing authorization", formalDefinition="The regulatory procedure for granting or amending a marketing authorization." )
    protected MedicinalProductAuthorizationProcedureComponent procedure;

    /**
     * Marketing status of the medicinal product, in contrast to marketing authorizaton.
     */
    @Child(name = "marketingStatus", type = {MarketingStatus.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Marketing status of the medicinal product, in contrast to marketing authorizaton", formalDefinition="Marketing status of the medicinal product, in contrast to marketing authorizaton." )
    protected List<MarketingStatus> marketingStatus;

    private static final long serialVersionUID = 593868878L;

  /**
   * Constructor
   */
    public MedicinalProductAuthorization() {
      super();
    }

  /**
   * Constructor
   */
    public MedicinalProductAuthorization(CodeableConcept legalStatusOfSupply, CodeableConcept status, DateTimeType statusDate, Period validityPeriod, DateTimeType internationalBirthDate, Reference holder, Reference regulator, MedicinalProductAuthorizationProcedureComponent procedure) {
      super();
      this.legalStatusOfSupply = legalStatusOfSupply;
      this.status = status;
      this.statusDate = statusDate;
      this.validityPeriod = validityPeriod;
      this.internationalBirthDate = internationalBirthDate;
      this.holder = holder;
      this.regulator = regulator;
      this.procedure = procedure;
    }

    /**
     * @return {@link #identifier} (Business identifier for the marketing authorization, as assigned by a regulator.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business identifier for the marketing authorization, as assigned by a regulator.)
     */
    public MedicinalProductAuthorization setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #country} (The country in which the marketing authorization has been granted.)
     */
    public List<CodeableConcept> getCountry() { 
      if (this.country == null)
        this.country = new ArrayList<CodeableConcept>();
      return this.country;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductAuthorization setCountry(List<CodeableConcept> theCountry) { 
      this.country = theCountry;
      return this;
    }

    public boolean hasCountry() { 
      if (this.country == null)
        return false;
      for (CodeableConcept item : this.country)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCountry() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.country == null)
        this.country = new ArrayList<CodeableConcept>();
      this.country.add(t);
      return t;
    }

    public MedicinalProductAuthorization addCountry(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.country == null)
        this.country = new ArrayList<CodeableConcept>();
      this.country.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #country}, creating it if it does not already exist
     */
    public CodeableConcept getCountryFirstRep() { 
      if (getCountry().isEmpty()) {
        addCountry();
      }
      return getCountry().get(0);
    }

    /**
     * @return {@link #legalStatusOfSupply} (The legal status of supply of the medicinal product as classified by the regulator.)
     */
    public CodeableConcept getLegalStatusOfSupply() { 
      if (this.legalStatusOfSupply == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.legalStatusOfSupply");
        else if (Configuration.doAutoCreate())
          this.legalStatusOfSupply = new CodeableConcept(); // cc
      return this.legalStatusOfSupply;
    }

    public boolean hasLegalStatusOfSupply() { 
      return this.legalStatusOfSupply != null && !this.legalStatusOfSupply.isEmpty();
    }

    /**
     * @param value {@link #legalStatusOfSupply} (The legal status of supply of the medicinal product as classified by the regulator.)
     */
    public MedicinalProductAuthorization setLegalStatusOfSupply(CodeableConcept value) { 
      this.legalStatusOfSupply = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the marketing authorization.)
     */
    public CodeableConcept getStatus() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.status");
        else if (Configuration.doAutoCreate())
          this.status = new CodeableConcept(); // cc
      return this.status;
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the marketing authorization.)
     */
    public MedicinalProductAuthorization setStatus(CodeableConcept value) { 
      this.status = value;
      return this;
    }

    /**
     * @return {@link #statusDate} (The date at which the given status has become applicable.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public DateTimeType getStatusDateElement() { 
      if (this.statusDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.statusDate");
        else if (Configuration.doAutoCreate())
          this.statusDate = new DateTimeType(); // bb
      return this.statusDate;
    }

    public boolean hasStatusDateElement() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    public boolean hasStatusDate() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    /**
     * @param value {@link #statusDate} (The date at which the given status has become applicable.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public MedicinalProductAuthorization setStatusDateElement(DateTimeType value) { 
      this.statusDate = value;
      return this;
    }

    /**
     * @return The date at which the given status has become applicable.
     */
    public Date getStatusDate() { 
      return this.statusDate == null ? null : this.statusDate.getValue();
    }

    /**
     * @param value The date at which the given status has become applicable.
     */
    public MedicinalProductAuthorization setStatusDate(Date value) { 
        if (this.statusDate == null)
          this.statusDate = new DateTimeType();
        this.statusDate.setValue(value);
      return this;
    }

    /**
     * @return {@link #restoreDate} (The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.). This is the underlying object with id, value and extensions. The accessor "getRestoreDate" gives direct access to the value
     */
    public DateTimeType getRestoreDateElement() { 
      if (this.restoreDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.restoreDate");
        else if (Configuration.doAutoCreate())
          this.restoreDate = new DateTimeType(); // bb
      return this.restoreDate;
    }

    public boolean hasRestoreDateElement() { 
      return this.restoreDate != null && !this.restoreDate.isEmpty();
    }

    public boolean hasRestoreDate() { 
      return this.restoreDate != null && !this.restoreDate.isEmpty();
    }

    /**
     * @param value {@link #restoreDate} (The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.). This is the underlying object with id, value and extensions. The accessor "getRestoreDate" gives direct access to the value
     */
    public MedicinalProductAuthorization setRestoreDateElement(DateTimeType value) { 
      this.restoreDate = value;
      return this;
    }

    /**
     * @return The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.
     */
    public Date getRestoreDate() { 
      return this.restoreDate == null ? null : this.restoreDate.getValue();
    }

    /**
     * @param value The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.
     */
    public MedicinalProductAuthorization setRestoreDate(Date value) { 
      if (value == null)
        this.restoreDate = null;
      else {
        if (this.restoreDate == null)
          this.restoreDate = new DateTimeType();
        this.restoreDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #validityPeriod} (The beginning of the time period in which the marketing authorization is in the specific status shall be specified A complete date consisting of day, month and year shall be specified using the ISO 8601 date format.)
     */
    public Period getValidityPeriod() { 
      if (this.validityPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.validityPeriod");
        else if (Configuration.doAutoCreate())
          this.validityPeriod = new Period(); // cc
      return this.validityPeriod;
    }

    public boolean hasValidityPeriod() { 
      return this.validityPeriod != null && !this.validityPeriod.isEmpty();
    }

    /**
     * @param value {@link #validityPeriod} (The beginning of the time period in which the marketing authorization is in the specific status shall be specified A complete date consisting of day, month and year shall be specified using the ISO 8601 date format.)
     */
    public MedicinalProductAuthorization setValidityPeriod(Period value) { 
      this.validityPeriod = value;
      return this;
    }

    /**
     * @return {@link #dataExclusivityPeriod} (A period of time after authorization before generic product applicatiosn can be submitted.)
     */
    public Period getDataExclusivityPeriod() { 
      if (this.dataExclusivityPeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.dataExclusivityPeriod");
        else if (Configuration.doAutoCreate())
          this.dataExclusivityPeriod = new Period(); // cc
      return this.dataExclusivityPeriod;
    }

    public boolean hasDataExclusivityPeriod() { 
      return this.dataExclusivityPeriod != null && !this.dataExclusivityPeriod.isEmpty();
    }

    /**
     * @param value {@link #dataExclusivityPeriod} (A period of time after authorization before generic product applicatiosn can be submitted.)
     */
    public MedicinalProductAuthorization setDataExclusivityPeriod(Period value) { 
      this.dataExclusivityPeriod = value;
      return this;
    }

    /**
     * @return {@link #dateOfFirstAuthorization} (The date when the first authorization was granted by a Medicines Regulatory Agency.). This is the underlying object with id, value and extensions. The accessor "getDateOfFirstAuthorization" gives direct access to the value
     */
    public DateTimeType getDateOfFirstAuthorizationElement() { 
      if (this.dateOfFirstAuthorization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.dateOfFirstAuthorization");
        else if (Configuration.doAutoCreate())
          this.dateOfFirstAuthorization = new DateTimeType(); // bb
      return this.dateOfFirstAuthorization;
    }

    public boolean hasDateOfFirstAuthorizationElement() { 
      return this.dateOfFirstAuthorization != null && !this.dateOfFirstAuthorization.isEmpty();
    }

    public boolean hasDateOfFirstAuthorization() { 
      return this.dateOfFirstAuthorization != null && !this.dateOfFirstAuthorization.isEmpty();
    }

    /**
     * @param value {@link #dateOfFirstAuthorization} (The date when the first authorization was granted by a Medicines Regulatory Agency.). This is the underlying object with id, value and extensions. The accessor "getDateOfFirstAuthorization" gives direct access to the value
     */
    public MedicinalProductAuthorization setDateOfFirstAuthorizationElement(DateTimeType value) { 
      this.dateOfFirstAuthorization = value;
      return this;
    }

    /**
     * @return The date when the first authorization was granted by a Medicines Regulatory Agency.
     */
    public Date getDateOfFirstAuthorization() { 
      return this.dateOfFirstAuthorization == null ? null : this.dateOfFirstAuthorization.getValue();
    }

    /**
     * @param value The date when the first authorization was granted by a Medicines Regulatory Agency.
     */
    public MedicinalProductAuthorization setDateOfFirstAuthorization(Date value) { 
      if (value == null)
        this.dateOfFirstAuthorization = null;
      else {
        if (this.dateOfFirstAuthorization == null)
          this.dateOfFirstAuthorization = new DateTimeType();
        this.dateOfFirstAuthorization.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #internationalBirthDate} (Date of first marketing authorization for a company's new medicinal product in any country in the World.). This is the underlying object with id, value and extensions. The accessor "getInternationalBirthDate" gives direct access to the value
     */
    public DateTimeType getInternationalBirthDateElement() { 
      if (this.internationalBirthDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.internationalBirthDate");
        else if (Configuration.doAutoCreate())
          this.internationalBirthDate = new DateTimeType(); // bb
      return this.internationalBirthDate;
    }

    public boolean hasInternationalBirthDateElement() { 
      return this.internationalBirthDate != null && !this.internationalBirthDate.isEmpty();
    }

    public boolean hasInternationalBirthDate() { 
      return this.internationalBirthDate != null && !this.internationalBirthDate.isEmpty();
    }

    /**
     * @param value {@link #internationalBirthDate} (Date of first marketing authorization for a company's new medicinal product in any country in the World.). This is the underlying object with id, value and extensions. The accessor "getInternationalBirthDate" gives direct access to the value
     */
    public MedicinalProductAuthorization setInternationalBirthDateElement(DateTimeType value) { 
      this.internationalBirthDate = value;
      return this;
    }

    /**
     * @return Date of first marketing authorization for a company's new medicinal product in any country in the World.
     */
    public Date getInternationalBirthDate() { 
      return this.internationalBirthDate == null ? null : this.internationalBirthDate.getValue();
    }

    /**
     * @param value Date of first marketing authorization for a company's new medicinal product in any country in the World.
     */
    public MedicinalProductAuthorization setInternationalBirthDate(Date value) { 
        if (this.internationalBirthDate == null)
          this.internationalBirthDate = new DateTimeType();
        this.internationalBirthDate.setValue(value);
      return this;
    }

    /**
     * @return {@link #jurisdictionalAuthorization} (Authorization in areas within a country.)
     */
    public List<MedicinalProductAuthorizationJurisdictionalAuthorizationComponent> getJurisdictionalAuthorization() { 
      if (this.jurisdictionalAuthorization == null)
        this.jurisdictionalAuthorization = new ArrayList<MedicinalProductAuthorizationJurisdictionalAuthorizationComponent>();
      return this.jurisdictionalAuthorization;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductAuthorization setJurisdictionalAuthorization(List<MedicinalProductAuthorizationJurisdictionalAuthorizationComponent> theJurisdictionalAuthorization) { 
      this.jurisdictionalAuthorization = theJurisdictionalAuthorization;
      return this;
    }

    public boolean hasJurisdictionalAuthorization() { 
      if (this.jurisdictionalAuthorization == null)
        return false;
      for (MedicinalProductAuthorizationJurisdictionalAuthorizationComponent item : this.jurisdictionalAuthorization)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent addJurisdictionalAuthorization() { //3
      MedicinalProductAuthorizationJurisdictionalAuthorizationComponent t = new MedicinalProductAuthorizationJurisdictionalAuthorizationComponent();
      if (this.jurisdictionalAuthorization == null)
        this.jurisdictionalAuthorization = new ArrayList<MedicinalProductAuthorizationJurisdictionalAuthorizationComponent>();
      this.jurisdictionalAuthorization.add(t);
      return t;
    }

    public MedicinalProductAuthorization addJurisdictionalAuthorization(MedicinalProductAuthorizationJurisdictionalAuthorizationComponent t) { //3
      if (t == null)
        return this;
      if (this.jurisdictionalAuthorization == null)
        this.jurisdictionalAuthorization = new ArrayList<MedicinalProductAuthorizationJurisdictionalAuthorizationComponent>();
      this.jurisdictionalAuthorization.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdictionalAuthorization}, creating it if it does not already exist
     */
    public MedicinalProductAuthorizationJurisdictionalAuthorizationComponent getJurisdictionalAuthorizationFirstRep() { 
      if (getJurisdictionalAuthorization().isEmpty()) {
        addJurisdictionalAuthorization();
      }
      return getJurisdictionalAuthorization().get(0);
    }

    /**
     * @return {@link #holder} (Marketing Authorization Holder.)
     */
    public Reference getHolder() { 
      if (this.holder == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.holder");
        else if (Configuration.doAutoCreate())
          this.holder = new Reference(); // cc
      return this.holder;
    }

    public boolean hasHolder() { 
      return this.holder != null && !this.holder.isEmpty();
    }

    /**
     * @param value {@link #holder} (Marketing Authorization Holder.)
     */
    public MedicinalProductAuthorization setHolder(Reference value) { 
      this.holder = value;
      return this;
    }

    /**
     * @return {@link #holder} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Marketing Authorization Holder.)
     */
    public Organization getHolderTarget() { 
      if (this.holderTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.holder");
        else if (Configuration.doAutoCreate())
          this.holderTarget = new Organization(); // aa
      return this.holderTarget;
    }

    /**
     * @param value {@link #holder} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Marketing Authorization Holder.)
     */
    public MedicinalProductAuthorization setHolderTarget(Organization value) { 
      this.holderTarget = value;
      return this;
    }

    /**
     * @return {@link #regulator} (Medicines Regulatory Agency.)
     */
    public Reference getRegulator() { 
      if (this.regulator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.regulator");
        else if (Configuration.doAutoCreate())
          this.regulator = new Reference(); // cc
      return this.regulator;
    }

    public boolean hasRegulator() { 
      return this.regulator != null && !this.regulator.isEmpty();
    }

    /**
     * @param value {@link #regulator} (Medicines Regulatory Agency.)
     */
    public MedicinalProductAuthorization setRegulator(Reference value) { 
      this.regulator = value;
      return this;
    }

    /**
     * @return {@link #regulator} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Medicines Regulatory Agency.)
     */
    public Organization getRegulatorTarget() { 
      if (this.regulatorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.regulator");
        else if (Configuration.doAutoCreate())
          this.regulatorTarget = new Organization(); // aa
      return this.regulatorTarget;
    }

    /**
     * @param value {@link #regulator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Medicines Regulatory Agency.)
     */
    public MedicinalProductAuthorization setRegulatorTarget(Organization value) { 
      this.regulatorTarget = value;
      return this;
    }

    /**
     * @return {@link #procedure} (The regulatory procedure for granting or amending a marketing authorization.)
     */
    public MedicinalProductAuthorizationProcedureComponent getProcedure() { 
      if (this.procedure == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductAuthorization.procedure");
        else if (Configuration.doAutoCreate())
          this.procedure = new MedicinalProductAuthorizationProcedureComponent(); // cc
      return this.procedure;
    }

    public boolean hasProcedure() { 
      return this.procedure != null && !this.procedure.isEmpty();
    }

    /**
     * @param value {@link #procedure} (The regulatory procedure for granting or amending a marketing authorization.)
     */
    public MedicinalProductAuthorization setProcedure(MedicinalProductAuthorizationProcedureComponent value) { 
      this.procedure = value;
      return this;
    }

    /**
     * @return {@link #marketingStatus} (Marketing status of the medicinal product, in contrast to marketing authorizaton.)
     */
    public List<MarketingStatus> getMarketingStatus() { 
      if (this.marketingStatus == null)
        this.marketingStatus = new ArrayList<MarketingStatus>();
      return this.marketingStatus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductAuthorization setMarketingStatus(List<MarketingStatus> theMarketingStatus) { 
      this.marketingStatus = theMarketingStatus;
      return this;
    }

    public boolean hasMarketingStatus() { 
      if (this.marketingStatus == null)
        return false;
      for (MarketingStatus item : this.marketingStatus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MarketingStatus addMarketingStatus() { //3
      MarketingStatus t = new MarketingStatus();
      if (this.marketingStatus == null)
        this.marketingStatus = new ArrayList<MarketingStatus>();
      this.marketingStatus.add(t);
      return t;
    }

    public MedicinalProductAuthorization addMarketingStatus(MarketingStatus t) { //3
      if (t == null)
        return this;
      if (this.marketingStatus == null)
        this.marketingStatus = new ArrayList<MarketingStatus>();
      this.marketingStatus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #marketingStatus}, creating it if it does not already exist
     */
    public MarketingStatus getMarketingStatusFirstRep() { 
      if (getMarketingStatus().isEmpty()) {
        addMarketingStatus();
      }
      return getMarketingStatus().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifier for the marketing authorization, as assigned by a regulator.", 0, 1, identifier));
        children.add(new Property("country", "CodeableConcept", "The country in which the marketing authorization has been granted.", 0, java.lang.Integer.MAX_VALUE, country));
        children.add(new Property("legalStatusOfSupply", "CodeableConcept", "The legal status of supply of the medicinal product as classified by the regulator.", 0, 1, legalStatusOfSupply));
        children.add(new Property("status", "CodeableConcept", "The status of the marketing authorization.", 0, 1, status));
        children.add(new Property("statusDate", "dateTime", "The date at which the given status has become applicable.", 0, 1, statusDate));
        children.add(new Property("restoreDate", "dateTime", "The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.", 0, 1, restoreDate));
        children.add(new Property("validityPeriod", "Period", "The beginning of the time period in which the marketing authorization is in the specific status shall be specified A complete date consisting of day, month and year shall be specified using the ISO 8601 date format.", 0, 1, validityPeriod));
        children.add(new Property("dataExclusivityPeriod", "Period", "A period of time after authorization before generic product applicatiosn can be submitted.", 0, 1, dataExclusivityPeriod));
        children.add(new Property("dateOfFirstAuthorization", "dateTime", "The date when the first authorization was granted by a Medicines Regulatory Agency.", 0, 1, dateOfFirstAuthorization));
        children.add(new Property("internationalBirthDate", "dateTime", "Date of first marketing authorization for a company's new medicinal product in any country in the World.", 0, 1, internationalBirthDate));
        children.add(new Property("jurisdictionalAuthorization", "", "Authorization in areas within a country.", 0, java.lang.Integer.MAX_VALUE, jurisdictionalAuthorization));
        children.add(new Property("holder", "Reference(Organization)", "Marketing Authorization Holder.", 0, 1, holder));
        children.add(new Property("regulator", "Reference(Organization)", "Medicines Regulatory Agency.", 0, 1, regulator));
        children.add(new Property("procedure", "", "The regulatory procedure for granting or amending a marketing authorization.", 0, 1, procedure));
        children.add(new Property("marketingStatus", "MarketingStatus", "Marketing status of the medicinal product, in contrast to marketing authorizaton.", 0, java.lang.Integer.MAX_VALUE, marketingStatus));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifier for the marketing authorization, as assigned by a regulator.", 0, 1, identifier);
        case 957831062: /*country*/  return new Property("country", "CodeableConcept", "The country in which the marketing authorization has been granted.", 0, java.lang.Integer.MAX_VALUE, country);
        case -844874031: /*legalStatusOfSupply*/  return new Property("legalStatusOfSupply", "CodeableConcept", "The legal status of supply of the medicinal product as classified by the regulator.", 0, 1, legalStatusOfSupply);
        case -892481550: /*status*/  return new Property("status", "CodeableConcept", "The status of the marketing authorization.", 0, 1, status);
        case 247524032: /*statusDate*/  return new Property("statusDate", "dateTime", "The date at which the given status has become applicable.", 0, 1, statusDate);
        case 329465692: /*restoreDate*/  return new Property("restoreDate", "dateTime", "The date when a suspended the marketing or the marketing authorization of the product is anticipated to be restored.", 0, 1, restoreDate);
        case -1434195053: /*validityPeriod*/  return new Property("validityPeriod", "Period", "The beginning of the time period in which the marketing authorization is in the specific status shall be specified A complete date consisting of day, month and year shall be specified using the ISO 8601 date format.", 0, 1, validityPeriod);
        case 1940655806: /*dataExclusivityPeriod*/  return new Property("dataExclusivityPeriod", "Period", "A period of time after authorization before generic product applicatiosn can be submitted.", 0, 1, dataExclusivityPeriod);
        case -1026933074: /*dateOfFirstAuthorization*/  return new Property("dateOfFirstAuthorization", "dateTime", "The date when the first authorization was granted by a Medicines Regulatory Agency.", 0, 1, dateOfFirstAuthorization);
        case 400069151: /*internationalBirthDate*/  return new Property("internationalBirthDate", "dateTime", "Date of first marketing authorization for a company's new medicinal product in any country in the World.", 0, 1, internationalBirthDate);
        case 1459432557: /*jurisdictionalAuthorization*/  return new Property("jurisdictionalAuthorization", "", "Authorization in areas within a country.", 0, java.lang.Integer.MAX_VALUE, jurisdictionalAuthorization);
        case -1211707988: /*holder*/  return new Property("holder", "Reference(Organization)", "Marketing Authorization Holder.", 0, 1, holder);
        case 414760449: /*regulator*/  return new Property("regulator", "Reference(Organization)", "Medicines Regulatory Agency.", 0, 1, regulator);
        case -1095204141: /*procedure*/  return new Property("procedure", "", "The regulatory procedure for granting or amending a marketing authorization.", 0, 1, procedure);
        case 70767032: /*marketingStatus*/  return new Property("marketingStatus", "MarketingStatus", "Marketing status of the medicinal product, in contrast to marketing authorizaton.", 0, java.lang.Integer.MAX_VALUE, marketingStatus);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 957831062: /*country*/ return this.country == null ? new Base[0] : this.country.toArray(new Base[this.country.size()]); // CodeableConcept
        case -844874031: /*legalStatusOfSupply*/ return this.legalStatusOfSupply == null ? new Base[0] : new Base[] {this.legalStatusOfSupply}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case 247524032: /*statusDate*/ return this.statusDate == null ? new Base[0] : new Base[] {this.statusDate}; // DateTimeType
        case 329465692: /*restoreDate*/ return this.restoreDate == null ? new Base[0] : new Base[] {this.restoreDate}; // DateTimeType
        case -1434195053: /*validityPeriod*/ return this.validityPeriod == null ? new Base[0] : new Base[] {this.validityPeriod}; // Period
        case 1940655806: /*dataExclusivityPeriod*/ return this.dataExclusivityPeriod == null ? new Base[0] : new Base[] {this.dataExclusivityPeriod}; // Period
        case -1026933074: /*dateOfFirstAuthorization*/ return this.dateOfFirstAuthorization == null ? new Base[0] : new Base[] {this.dateOfFirstAuthorization}; // DateTimeType
        case 400069151: /*internationalBirthDate*/ return this.internationalBirthDate == null ? new Base[0] : new Base[] {this.internationalBirthDate}; // DateTimeType
        case 1459432557: /*jurisdictionalAuthorization*/ return this.jurisdictionalAuthorization == null ? new Base[0] : this.jurisdictionalAuthorization.toArray(new Base[this.jurisdictionalAuthorization.size()]); // MedicinalProductAuthorizationJurisdictionalAuthorizationComponent
        case -1211707988: /*holder*/ return this.holder == null ? new Base[0] : new Base[] {this.holder}; // Reference
        case 414760449: /*regulator*/ return this.regulator == null ? new Base[0] : new Base[] {this.regulator}; // Reference
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : new Base[] {this.procedure}; // MedicinalProductAuthorizationProcedureComponent
        case 70767032: /*marketingStatus*/ return this.marketingStatus == null ? new Base[0] : this.marketingStatus.toArray(new Base[this.marketingStatus.size()]); // MarketingStatus
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 957831062: // country
          this.getCountry().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -844874031: // legalStatusOfSupply
          this.legalStatusOfSupply = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 247524032: // statusDate
          this.statusDate = castToDateTime(value); // DateTimeType
          return value;
        case 329465692: // restoreDate
          this.restoreDate = castToDateTime(value); // DateTimeType
          return value;
        case -1434195053: // validityPeriod
          this.validityPeriod = castToPeriod(value); // Period
          return value;
        case 1940655806: // dataExclusivityPeriod
          this.dataExclusivityPeriod = castToPeriod(value); // Period
          return value;
        case -1026933074: // dateOfFirstAuthorization
          this.dateOfFirstAuthorization = castToDateTime(value); // DateTimeType
          return value;
        case 400069151: // internationalBirthDate
          this.internationalBirthDate = castToDateTime(value); // DateTimeType
          return value;
        case 1459432557: // jurisdictionalAuthorization
          this.getJurisdictionalAuthorization().add((MedicinalProductAuthorizationJurisdictionalAuthorizationComponent) value); // MedicinalProductAuthorizationJurisdictionalAuthorizationComponent
          return value;
        case -1211707988: // holder
          this.holder = castToReference(value); // Reference
          return value;
        case 414760449: // regulator
          this.regulator = castToReference(value); // Reference
          return value;
        case -1095204141: // procedure
          this.procedure = (MedicinalProductAuthorizationProcedureComponent) value; // MedicinalProductAuthorizationProcedureComponent
          return value;
        case 70767032: // marketingStatus
          this.getMarketingStatus().add(castToMarketingStatus(value)); // MarketingStatus
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("country")) {
          this.getCountry().add(castToCodeableConcept(value));
        } else if (name.equals("legalStatusOfSupply")) {
          this.legalStatusOfSupply = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("statusDate")) {
          this.statusDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("restoreDate")) {
          this.restoreDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("validityPeriod")) {
          this.validityPeriod = castToPeriod(value); // Period
        } else if (name.equals("dataExclusivityPeriod")) {
          this.dataExclusivityPeriod = castToPeriod(value); // Period
        } else if (name.equals("dateOfFirstAuthorization")) {
          this.dateOfFirstAuthorization = castToDateTime(value); // DateTimeType
        } else if (name.equals("internationalBirthDate")) {
          this.internationalBirthDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("jurisdictionalAuthorization")) {
          this.getJurisdictionalAuthorization().add((MedicinalProductAuthorizationJurisdictionalAuthorizationComponent) value);
        } else if (name.equals("holder")) {
          this.holder = castToReference(value); // Reference
        } else if (name.equals("regulator")) {
          this.regulator = castToReference(value); // Reference
        } else if (name.equals("procedure")) {
          this.procedure = (MedicinalProductAuthorizationProcedureComponent) value; // MedicinalProductAuthorizationProcedureComponent
        } else if (name.equals("marketingStatus")) {
          this.getMarketingStatus().add(castToMarketingStatus(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 957831062:  return addCountry(); 
        case -844874031:  return getLegalStatusOfSupply(); 
        case -892481550:  return getStatus(); 
        case 247524032:  return getStatusDateElement();
        case 329465692:  return getRestoreDateElement();
        case -1434195053:  return getValidityPeriod(); 
        case 1940655806:  return getDataExclusivityPeriod(); 
        case -1026933074:  return getDateOfFirstAuthorizationElement();
        case 400069151:  return getInternationalBirthDateElement();
        case 1459432557:  return addJurisdictionalAuthorization(); 
        case -1211707988:  return getHolder(); 
        case 414760449:  return getRegulator(); 
        case -1095204141:  return getProcedure(); 
        case 70767032:  return addMarketingStatus(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 957831062: /*country*/ return new String[] {"CodeableConcept"};
        case -844874031: /*legalStatusOfSupply*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
        case 247524032: /*statusDate*/ return new String[] {"dateTime"};
        case 329465692: /*restoreDate*/ return new String[] {"dateTime"};
        case -1434195053: /*validityPeriod*/ return new String[] {"Period"};
        case 1940655806: /*dataExclusivityPeriod*/ return new String[] {"Period"};
        case -1026933074: /*dateOfFirstAuthorization*/ return new String[] {"dateTime"};
        case 400069151: /*internationalBirthDate*/ return new String[] {"dateTime"};
        case 1459432557: /*jurisdictionalAuthorization*/ return new String[] {};
        case -1211707988: /*holder*/ return new String[] {"Reference"};
        case 414760449: /*regulator*/ return new String[] {"Reference"};
        case -1095204141: /*procedure*/ return new String[] {};
        case 70767032: /*marketingStatus*/ return new String[] {"MarketingStatus"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("country")) {
          return addCountry();
        }
        else if (name.equals("legalStatusOfSupply")) {
          this.legalStatusOfSupply = new CodeableConcept();
          return this.legalStatusOfSupply;
        }
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("statusDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductAuthorization.statusDate");
        }
        else if (name.equals("restoreDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductAuthorization.restoreDate");
        }
        else if (name.equals("validityPeriod")) {
          this.validityPeriod = new Period();
          return this.validityPeriod;
        }
        else if (name.equals("dataExclusivityPeriod")) {
          this.dataExclusivityPeriod = new Period();
          return this.dataExclusivityPeriod;
        }
        else if (name.equals("dateOfFirstAuthorization")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductAuthorization.dateOfFirstAuthorization");
        }
        else if (name.equals("internationalBirthDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductAuthorization.internationalBirthDate");
        }
        else if (name.equals("jurisdictionalAuthorization")) {
          return addJurisdictionalAuthorization();
        }
        else if (name.equals("holder")) {
          this.holder = new Reference();
          return this.holder;
        }
        else if (name.equals("regulator")) {
          this.regulator = new Reference();
          return this.regulator;
        }
        else if (name.equals("procedure")) {
          this.procedure = new MedicinalProductAuthorizationProcedureComponent();
          return this.procedure;
        }
        else if (name.equals("marketingStatus")) {
          return addMarketingStatus();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductAuthorization";

  }

      public MedicinalProductAuthorization copy() {
        MedicinalProductAuthorization dst = new MedicinalProductAuthorization();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (country != null) {
          dst.country = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : country)
            dst.country.add(i.copy());
        };
        dst.legalStatusOfSupply = legalStatusOfSupply == null ? null : legalStatusOfSupply.copy();
        dst.status = status == null ? null : status.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        dst.restoreDate = restoreDate == null ? null : restoreDate.copy();
        dst.validityPeriod = validityPeriod == null ? null : validityPeriod.copy();
        dst.dataExclusivityPeriod = dataExclusivityPeriod == null ? null : dataExclusivityPeriod.copy();
        dst.dateOfFirstAuthorization = dateOfFirstAuthorization == null ? null : dateOfFirstAuthorization.copy();
        dst.internationalBirthDate = internationalBirthDate == null ? null : internationalBirthDate.copy();
        if (jurisdictionalAuthorization != null) {
          dst.jurisdictionalAuthorization = new ArrayList<MedicinalProductAuthorizationJurisdictionalAuthorizationComponent>();
          for (MedicinalProductAuthorizationJurisdictionalAuthorizationComponent i : jurisdictionalAuthorization)
            dst.jurisdictionalAuthorization.add(i.copy());
        };
        dst.holder = holder == null ? null : holder.copy();
        dst.regulator = regulator == null ? null : regulator.copy();
        dst.procedure = procedure == null ? null : procedure.copy();
        if (marketingStatus != null) {
          dst.marketingStatus = new ArrayList<MarketingStatus>();
          for (MarketingStatus i : marketingStatus)
            dst.marketingStatus.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductAuthorization typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorization))
          return false;
        MedicinalProductAuthorization o = (MedicinalProductAuthorization) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(country, o.country, true) && compareDeep(legalStatusOfSupply, o.legalStatusOfSupply, true)
           && compareDeep(status, o.status, true) && compareDeep(statusDate, o.statusDate, true) && compareDeep(restoreDate, o.restoreDate, true)
           && compareDeep(validityPeriod, o.validityPeriod, true) && compareDeep(dataExclusivityPeriod, o.dataExclusivityPeriod, true)
           && compareDeep(dateOfFirstAuthorization, o.dateOfFirstAuthorization, true) && compareDeep(internationalBirthDate, o.internationalBirthDate, true)
           && compareDeep(jurisdictionalAuthorization, o.jurisdictionalAuthorization, true) && compareDeep(holder, o.holder, true)
           && compareDeep(regulator, o.regulator, true) && compareDeep(procedure, o.procedure, true) && compareDeep(marketingStatus, o.marketingStatus, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductAuthorization))
          return false;
        MedicinalProductAuthorization o = (MedicinalProductAuthorization) other_;
        return compareValues(statusDate, o.statusDate, true) && compareValues(restoreDate, o.restoreDate, true)
           && compareValues(dateOfFirstAuthorization, o.dateOfFirstAuthorization, true) && compareValues(internationalBirthDate, o.internationalBirthDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, country, legalStatusOfSupply
          , status, statusDate, restoreDate, validityPeriod, dataExclusivityPeriod, dateOfFirstAuthorization
          , internationalBirthDate, jurisdictionalAuthorization, holder, regulator, procedure
          , marketingStatus);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductAuthorization;
   }


}

