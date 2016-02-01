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
 * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
 */
@ResourceDef(name="Contract", profile="http://hl7.org/fhir/Profile/Contract")
public class Contract extends DomainResource {

    @Block()
    public static class PartyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Who or what parties are assigned roles in this Contract.
         */
        @Child(name = "entity", type = {Contract.class, Device.class, Group.class, Location.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class, Substance.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Party Type", formalDefinition="Who or what parties are assigned roles in this Contract." )
        protected Reference entity;

        /**
         * The actual object that is the target of the reference (Who or what parties are assigned roles in this Contract.)
         */
        protected Resource entityTarget;

        /**
         * Role type of party assigned roles in this Contract.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract  Party Role", formalDefinition="Role type of party assigned roles in this Contract." )
        protected List<CodeableConcept> role;

        private static final long serialVersionUID = 1371245689L;

    /**
     * Constructor
     */
      public PartyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PartyComponent(Reference entity) {
        super();
        this.entity = entity;
      }

        /**
         * @return {@link #entity} (Who or what parties are assigned roles in this Contract.)
         */
        public Reference getEntity() { 
          if (this.entity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PartyComponent.entity");
            else if (Configuration.doAutoCreate())
              this.entity = new Reference(); // cc
          return this.entity;
        }

        public boolean hasEntity() { 
          return this.entity != null && !this.entity.isEmpty();
        }

        /**
         * @param value {@link #entity} (Who or what parties are assigned roles in this Contract.)
         */
        public PartyComponent setEntity(Reference value) { 
          this.entity = value;
          return this;
        }

        /**
         * @return {@link #entity} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what parties are assigned roles in this Contract.)
         */
        public Resource getEntityTarget() { 
          return this.entityTarget;
        }

        /**
         * @param value {@link #entity} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what parties are assigned roles in this Contract.)
         */
        public PartyComponent setEntityTarget(Resource value) { 
          this.entityTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (Role type of party assigned roles in this Contract.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        public boolean hasRole() { 
          if (this.role == null)
            return false;
          for (CodeableConcept item : this.role)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #role} (Role type of party assigned roles in this Contract.)
         */
    // syntactic sugar
        public CodeableConcept addRole() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return t;
        }

    // syntactic sugar
        public PartyComponent addRole(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("entity", "Reference(Contract|Device|Group|Location|Organization|Patient|Practitioner|RelatedPerson|Substance)", "Who or what parties are assigned roles in this Contract.", 0, java.lang.Integer.MAX_VALUE, entity));
          childrenList.add(new Property("role", "CodeableConcept", "Role type of party assigned roles in this Contract.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("entity"))
          this.entity = castToReference(value); // Reference
        else if (name.equals("role"))
          this.getRole().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("entity")) {
          this.entity = new Reference();
          return this.entity;
        }
        else if (name.equals("role")) {
          return addRole();
        }
        else
          return super.addChild(name);
      }

      public PartyComponent copy() {
        PartyComponent dst = new PartyComponent();
        copyValues(dst);
        dst.entity = entity == null ? null : entity.copy();
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PartyComponent))
          return false;
        PartyComponent o = (PartyComponent) other;
        return compareDeep(entity, o.entity, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PartyComponent))
          return false;
        PartyComponent o = (PartyComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (entity == null || entity.isEmpty()) && (role == null || role.isEmpty())
          ;
      }

  public String fhirType() {
    return "Contract.party";

  }

  }

    @Block()
    public static class ValuedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specific type of Contract Valued Item that may be priced.
         */
        @Child(name = "entity", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Type", formalDefinition="Specific type of Contract Valued Item that may be priced." )
        protected Type entity;

        /**
         * Identifies a Contract Valued Item instance.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Identifier", formalDefinition="Identifies a Contract Valued Item instance." )
        protected Identifier identifier;

        /**
         * Indicates the time during which this Contract ValuedItem information is effective.
         */
        @Child(name = "effectiveTime", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Effective Tiem", formalDefinition="Indicates the time during which this Contract ValuedItem information is effective." )
        protected DateTimeType effectiveTime;

        /**
         * Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of Contract Valued Items", formalDefinition="Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances." )
        protected SimpleQuantity quantity;

        /**
         * A Contract Valued Item unit valuation measure.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item fee, charge, or cost", formalDefinition="A Contract Valued Item unit valuation measure." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Price Scaling Factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Difficulty Scaling Factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total Contract Valued Item Value", formalDefinition="Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        private static final long serialVersionUID = 1782449516L;

    /**
     * Constructor
     */
      public ValuedItemComponent() {
        super();
      }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public Type getEntity() { 
          return this.entity;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public CodeableConcept getEntityCodeableConcept() throws FHIRException { 
          if (!(this.entity instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (CodeableConcept) this.entity;
        }

        public boolean hasEntityCodeableConcept() { 
          return this.entity instanceof CodeableConcept;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public Reference getEntityReference() throws FHIRException { 
          if (!(this.entity instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (Reference) this.entity;
        }

        public boolean hasEntityReference() { 
          return this.entity instanceof Reference;
        }

        public boolean hasEntity() { 
          return this.entity != null && !this.entity.isEmpty();
        }

        /**
         * @param value {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public ValuedItemComponent setEntity(Type value) { 
          this.entity = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Identifies a Contract Valued Item instance.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifies a Contract Valued Item instance.)
         */
        public ValuedItemComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #effectiveTime} (Indicates the time during which this Contract ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public DateTimeType getEffectiveTimeElement() { 
          if (this.effectiveTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.effectiveTime");
            else if (Configuration.doAutoCreate())
              this.effectiveTime = new DateTimeType(); // bb
          return this.effectiveTime;
        }

        public boolean hasEffectiveTimeElement() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        public boolean hasEffectiveTime() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        /**
         * @param value {@link #effectiveTime} (Indicates the time during which this Contract ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public ValuedItemComponent setEffectiveTimeElement(DateTimeType value) { 
          this.effectiveTime = value;
          return this;
        }

        /**
         * @return Indicates the time during which this Contract ValuedItem information is effective.
         */
        public Date getEffectiveTime() { 
          return this.effectiveTime == null ? null : this.effectiveTime.getValue();
        }

        /**
         * @param value Indicates the time during which this Contract ValuedItem information is effective.
         */
        public ValuedItemComponent setEffectiveTime(Date value) { 
          if (value == null)
            this.effectiveTime = null;
          else {
            if (this.effectiveTime == null)
              this.effectiveTime = new DateTimeType();
            this.effectiveTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #quantity} (Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.)
         */
        public ValuedItemComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (A Contract Valued Item unit valuation measure.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (A Contract Valued Item unit valuation measure.)
         */
        public ValuedItemComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public ValuedItemComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType(); // bb
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public ValuedItemComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(double value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public ValuedItemComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, java.lang.Integer.MAX_VALUE, entity));
          childrenList.add(new Property("identifier", "Identifier", "Identifies a Contract Valued Item instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("effectiveTime", "dateTime", "Indicates the time during which this Contract ValuedItem information is effective.", 0, java.lang.Integer.MAX_VALUE, effectiveTime));
          childrenList.add(new Property("quantity", "SimpleQuantity", "Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "A Contract Valued Item unit valuation measure.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("entity[x]"))
          this.entity = (Type) value; // Type
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("effectiveTime"))
          this.effectiveTime = castToDateTime(value); // DateTimeType
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("unitPrice"))
          this.unitPrice = castToMoney(value); // Money
        else if (name.equals("factor"))
          this.factor = castToDecimal(value); // DecimalType
        else if (name.equals("points"))
          this.points = castToDecimal(value); // DecimalType
        else if (name.equals("net"))
          this.net = castToMoney(value); // Money
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("entityCodeableConcept")) {
          this.entity = new CodeableConcept();
          return this.entity;
        }
        else if (name.equals("entityReference")) {
          this.entity = new Reference();
          return this.entity;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("effectiveTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.effectiveTime");
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else
          return super.addChild(name);
      }

      public ValuedItemComponent copy() {
        ValuedItemComponent dst = new ValuedItemComponent();
        copyValues(dst);
        dst.entity = entity == null ? null : entity.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.effectiveTime = effectiveTime == null ? null : effectiveTime.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ValuedItemComponent))
          return false;
        ValuedItemComponent o = (ValuedItemComponent) other;
        return compareDeep(entity, o.entity, true) && compareDeep(identifier, o.identifier, true) && compareDeep(effectiveTime, o.effectiveTime, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ValuedItemComponent))
          return false;
        ValuedItemComponent o = (ValuedItemComponent) other;
        return compareValues(effectiveTime, o.effectiveTime, true) && compareValues(factor, o.factor, true)
           && compareValues(points, o.points, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (entity == null || entity.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (effectiveTime == null || effectiveTime.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (unitPrice == null || unitPrice.isEmpty()) && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty())
           && (net == null || net.isEmpty());
      }

  public String fhirType() {
    return "Contract.valuedItem";

  }

  }

    @Block()
    public static class SignatoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Role of this Contract signer, e.g. notary, grantee.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Signer Type", formalDefinition="Role of this Contract signer, e.g. notary, grantee." )
        protected Coding type;

        /**
         * Party which is a signator to this Contract.
         */
        @Child(name = "party", type = {Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Signatory Party", formalDefinition="Party which is a signator to this Contract." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (Party which is a signator to this Contract.)
         */
        protected Resource partyTarget;

        /**
         * Legally binding Contract DSIG signature contents in Base64.
         */
        @Child(name = "signature", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Documentation Signature", formalDefinition="Legally binding Contract DSIG signature contents in Base64." )
        protected StringType signature;

        private static final long serialVersionUID = -1870392043L;

    /**
     * Constructor
     */
      public SignatoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SignatoryComponent(Coding type, Reference party, StringType signature) {
        super();
        this.type = type;
        this.party = party;
        this.signature = signature;
      }

        /**
         * @return {@link #type} (Role of this Contract signer, e.g. notary, grantee.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SignatoryComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Role of this Contract signer, e.g. notary, grantee.)
         */
        public SignatoryComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #party} (Party which is a signator to this Contract.)
         */
        public Reference getParty() { 
          if (this.party == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SignatoryComponent.party");
            else if (Configuration.doAutoCreate())
              this.party = new Reference(); // cc
          return this.party;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (Party which is a signator to this Contract.)
         */
        public SignatoryComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Party which is a signator to this Contract.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Party which is a signator to this Contract.)
         */
        public SignatoryComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        /**
         * @return {@link #signature} (Legally binding Contract DSIG signature contents in Base64.). This is the underlying object with id, value and extensions. The accessor "getSignature" gives direct access to the value
         */
        public StringType getSignatureElement() { 
          if (this.signature == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SignatoryComponent.signature");
            else if (Configuration.doAutoCreate())
              this.signature = new StringType(); // bb
          return this.signature;
        }

        public boolean hasSignatureElement() { 
          return this.signature != null && !this.signature.isEmpty();
        }

        public boolean hasSignature() { 
          return this.signature != null && !this.signature.isEmpty();
        }

        /**
         * @param value {@link #signature} (Legally binding Contract DSIG signature contents in Base64.). This is the underlying object with id, value and extensions. The accessor "getSignature" gives direct access to the value
         */
        public SignatoryComponent setSignatureElement(StringType value) { 
          this.signature = value;
          return this;
        }

        /**
         * @return Legally binding Contract DSIG signature contents in Base64.
         */
        public String getSignature() { 
          return this.signature == null ? null : this.signature.getValue();
        }

        /**
         * @param value Legally binding Contract DSIG signature contents in Base64.
         */
        public SignatoryComponent setSignature(String value) { 
            if (this.signature == null)
              this.signature = new StringType();
            this.signature.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Role of this Contract signer, e.g. notary, grantee.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("party", "Reference(Organization|Patient|Practitioner|RelatedPerson)", "Party which is a signator to this Contract.", 0, java.lang.Integer.MAX_VALUE, party));
          childrenList.add(new Property("signature", "string", "Legally binding Contract DSIG signature contents in Base64.", 0, java.lang.Integer.MAX_VALUE, signature));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("party"))
          this.party = castToReference(value); // Reference
        else if (name.equals("signature"))
          this.signature = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("party")) {
          this.party = new Reference();
          return this.party;
        }
        else if (name.equals("signature")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.signature");
        }
        else
          return super.addChild(name);
      }

      public SignatoryComponent copy() {
        SignatoryComponent dst = new SignatoryComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.party = party == null ? null : party.copy();
        dst.signature = signature == null ? null : signature.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SignatoryComponent))
          return false;
        SignatoryComponent o = (SignatoryComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(party, o.party, true) && compareDeep(signature, o.signature, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SignatoryComponent))
          return false;
        SignatoryComponent o = (SignatoryComponent) other;
        return compareValues(signature, o.signature, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (party == null || party.isEmpty())
           && (signature == null || signature.isEmpty());
      }

  public String fhirType() {
    return "Contract.signer";

  }

  }

    @Block()
    public static class TermComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique identifier for this particular Contract Provision.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term identifier", formalDefinition="Unique identifier for this particular Contract Provision." )
        protected Identifier identifier;

        /**
         * When this Contract Provision was issued.
         */
        @Child(name = "issued", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Issue Date Time", formalDefinition="When this Contract Provision was issued." )
        protected DateTimeType issued;

        /**
         * Relevant time or time-period when this Contract Provision is applicable.
         */
        @Child(name = "applies", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Effective Time", formalDefinition="Relevant time or time-period when this Contract Provision is applicable." )
        protected Period applies;

        /**
         * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Type", formalDefinition="Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit." )
        protected CodeableConcept type;

        /**
         * Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.
         */
        @Child(name = "subType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Subtype", formalDefinition="Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment." )
        protected CodeableConcept subType;

        /**
         * The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services.
         */
        @Child(name = "topic", type = {}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Topic of this Contract Term", formalDefinition="The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services." )
        protected Reference topic;

        /**
         * The actual object that is the target of the reference (The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services.)
         */
        protected Resource topicTarget;

        /**
         * Who or what this Contract Provision is about.
         */
        @Child(name = "subject", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Subject of this Contract Term", formalDefinition="Who or what this Contract Provision is about." )
        protected Reference subject;

        /**
         * The actual object that is the target of the reference (Who or what this Contract Provision is about.)
         */
        protected Resource subjectTarget;

        /**
         * Action stipulated by this Contract Provision.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Action", formalDefinition="Action stipulated by this Contract Provision." )
        protected List<CodeableConcept> action;

        /**
         * Reason or purpose for the action stipulated by this Contract Provision.
         */
        @Child(name = "actionReason", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Action Reason", formalDefinition="Reason or purpose for the action stipulated by this Contract Provision." )
        protected List<CodeableConcept> actionReason;

        /**
         * Parties to the Contract, specifically, the grantor(s) and grantee(s) such as a person or an organization which is bound by the contract to the actions specified on the Contract.topic and the terms of the Contract that either extend or restrict the overall action on the topic by, for example, stipulating specific policies or obligations constraining actions and action reasons permitted or denied on a subset of the Contract topic, actors, subject(s), and extending or limiting the Contract.period of applicability or valuation of items under consideration.
         */
        @Child(name = "party", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Party List", formalDefinition="Parties to the Contract, specifically, the grantor(s) and grantee(s) such as a person or an organization which is bound by the contract to the actions specified on the Contract.topic and the terms of the Contract that either extend or restrict the overall action on the topic by, for example, stipulating specific policies or obligations constraining actions and action reasons permitted or denied on a subset of the Contract topic, actors, subject(s), and extending or limiting the Contract.period of applicability or valuation of items under consideration." )
        protected List<TermPartyComponent> party;

        /**
         * Human readable form of this Contract Provision.
         */
        @Child(name = "text", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human readable Contract term text", formalDefinition="Human readable form of this Contract Provision." )
        protected StringType text;

        /**
         * Contract Provision Valued Item List.
         */
        @Child(name = "valuedItem", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item", formalDefinition="Contract Provision Valued Item List." )
        protected List<TermValuedItemComponent> valuedItem;

        /**
         * Nested group of Contract Provisions.
         */
        @Child(name = "group", type = {TermComponent.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Contract Term Group", formalDefinition="Nested group of Contract Provisions." )
        protected List<TermComponent> group;

        private static final long serialVersionUID = 1105219555L;

    /**
     * Constructor
     */
      public TermComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Unique identifier for this particular Contract Provision.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Unique identifier for this particular Contract Provision.)
         */
        public TermComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #issued} (When this Contract Provision was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public DateTimeType getIssuedElement() { 
          if (this.issued == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.issued");
            else if (Configuration.doAutoCreate())
              this.issued = new DateTimeType(); // bb
          return this.issued;
        }

        public boolean hasIssuedElement() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        public boolean hasIssued() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        /**
         * @param value {@link #issued} (When this Contract Provision was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public TermComponent setIssuedElement(DateTimeType value) { 
          this.issued = value;
          return this;
        }

        /**
         * @return When this Contract Provision was issued.
         */
        public Date getIssued() { 
          return this.issued == null ? null : this.issued.getValue();
        }

        /**
         * @param value When this Contract Provision was issued.
         */
        public TermComponent setIssued(Date value) { 
          if (value == null)
            this.issued = null;
          else {
            if (this.issued == null)
              this.issued = new DateTimeType();
            this.issued.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #applies} (Relevant time or time-period when this Contract Provision is applicable.)
         */
        public Period getApplies() { 
          if (this.applies == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.applies");
            else if (Configuration.doAutoCreate())
              this.applies = new Period(); // cc
          return this.applies;
        }

        public boolean hasApplies() { 
          return this.applies != null && !this.applies.isEmpty();
        }

        /**
         * @param value {@link #applies} (Relevant time or time-period when this Contract Provision is applicable.)
         */
        public TermComponent setApplies(Period value) { 
          this.applies = value;
          return this;
        }

        /**
         * @return {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public TermComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subType} (Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.)
         */
        public CodeableConcept getSubType() { 
          if (this.subType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.subType");
            else if (Configuration.doAutoCreate())
              this.subType = new CodeableConcept(); // cc
          return this.subType;
        }

        public boolean hasSubType() { 
          return this.subType != null && !this.subType.isEmpty();
        }

        /**
         * @param value {@link #subType} (Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.)
         */
        public TermComponent setSubType(CodeableConcept value) { 
          this.subType = value;
          return this;
        }

        /**
         * @return {@link #topic} (The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services.)
         */
        public Reference getTopic() { 
          if (this.topic == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.topic");
            else if (Configuration.doAutoCreate())
              this.topic = new Reference(); // cc
          return this.topic;
        }

        public boolean hasTopic() { 
          return this.topic != null && !this.topic.isEmpty();
        }

        /**
         * @param value {@link #topic} (The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services.)
         */
        public TermComponent setTopic(Reference value) { 
          this.topic = value;
          return this;
        }

        /**
         * @return {@link #topic} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services.)
         */
        public Resource getTopicTarget() { 
          return this.topicTarget;
        }

        /**
         * @param value {@link #topic} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services.)
         */
        public TermComponent setTopicTarget(Resource value) { 
          this.topicTarget = value;
          return this;
        }

        /**
         * @return {@link #subject} (Who or what this Contract Provision is about.)
         */
        public Reference getSubject() { 
          if (this.subject == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.subject");
            else if (Configuration.doAutoCreate())
              this.subject = new Reference(); // cc
          return this.subject;
        }

        public boolean hasSubject() { 
          return this.subject != null && !this.subject.isEmpty();
        }

        /**
         * @param value {@link #subject} (Who or what this Contract Provision is about.)
         */
        public TermComponent setSubject(Reference value) { 
          this.subject = value;
          return this;
        }

        /**
         * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what this Contract Provision is about.)
         */
        public Resource getSubjectTarget() { 
          return this.subjectTarget;
        }

        /**
         * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what this Contract Provision is about.)
         */
        public TermComponent setSubjectTarget(Resource value) { 
          this.subjectTarget = value;
          return this;
        }

        /**
         * @return {@link #action} (Action stipulated by this Contract Provision.)
         */
        public List<CodeableConcept> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          return this.action;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (CodeableConcept item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (Action stipulated by this Contract Provision.)
         */
    // syntactic sugar
        public CodeableConcept addAction() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public TermComponent addAction(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return this;
        }

        /**
         * @return {@link #actionReason} (Reason or purpose for the action stipulated by this Contract Provision.)
         */
        public List<CodeableConcept> getActionReason() { 
          if (this.actionReason == null)
            this.actionReason = new ArrayList<CodeableConcept>();
          return this.actionReason;
        }

        public boolean hasActionReason() { 
          if (this.actionReason == null)
            return false;
          for (CodeableConcept item : this.actionReason)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #actionReason} (Reason or purpose for the action stipulated by this Contract Provision.)
         */
    // syntactic sugar
        public CodeableConcept addActionReason() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.actionReason == null)
            this.actionReason = new ArrayList<CodeableConcept>();
          this.actionReason.add(t);
          return t;
        }

    // syntactic sugar
        public TermComponent addActionReason(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.actionReason == null)
            this.actionReason = new ArrayList<CodeableConcept>();
          this.actionReason.add(t);
          return this;
        }

        /**
         * @return {@link #party} (Parties to the Contract, specifically, the grantor(s) and grantee(s) such as a person or an organization which is bound by the contract to the actions specified on the Contract.topic and the terms of the Contract that either extend or restrict the overall action on the topic by, for example, stipulating specific policies or obligations constraining actions and action reasons permitted or denied on a subset of the Contract topic, actors, subject(s), and extending or limiting the Contract.period of applicability or valuation of items under consideration.)
         */
        public List<TermPartyComponent> getParty() { 
          if (this.party == null)
            this.party = new ArrayList<TermPartyComponent>();
          return this.party;
        }

        public boolean hasParty() { 
          if (this.party == null)
            return false;
          for (TermPartyComponent item : this.party)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #party} (Parties to the Contract, specifically, the grantor(s) and grantee(s) such as a person or an organization which is bound by the contract to the actions specified on the Contract.topic and the terms of the Contract that either extend or restrict the overall action on the topic by, for example, stipulating specific policies or obligations constraining actions and action reasons permitted or denied on a subset of the Contract topic, actors, subject(s), and extending or limiting the Contract.period of applicability or valuation of items under consideration.)
         */
    // syntactic sugar
        public TermPartyComponent addParty() { //3
          TermPartyComponent t = new TermPartyComponent();
          if (this.party == null)
            this.party = new ArrayList<TermPartyComponent>();
          this.party.add(t);
          return t;
        }

    // syntactic sugar
        public TermComponent addParty(TermPartyComponent t) { //3
          if (t == null)
            return this;
          if (this.party == null)
            this.party = new ArrayList<TermPartyComponent>();
          this.party.add(t);
          return this;
        }

        /**
         * @return {@link #text} (Human readable form of this Contract Provision.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Human readable form of this Contract Provision.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public TermComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Human readable form of this Contract Provision.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Human readable form of this Contract Provision.
         */
        public TermComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #valuedItem} (Contract Provision Valued Item List.)
         */
        public List<TermValuedItemComponent> getValuedItem() { 
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<TermValuedItemComponent>();
          return this.valuedItem;
        }

        public boolean hasValuedItem() { 
          if (this.valuedItem == null)
            return false;
          for (TermValuedItemComponent item : this.valuedItem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #valuedItem} (Contract Provision Valued Item List.)
         */
    // syntactic sugar
        public TermValuedItemComponent addValuedItem() { //3
          TermValuedItemComponent t = new TermValuedItemComponent();
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<TermValuedItemComponent>();
          this.valuedItem.add(t);
          return t;
        }

    // syntactic sugar
        public TermComponent addValuedItem(TermValuedItemComponent t) { //3
          if (t == null)
            return this;
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<TermValuedItemComponent>();
          this.valuedItem.add(t);
          return this;
        }

        /**
         * @return {@link #group} (Nested group of Contract Provisions.)
         */
        public List<TermComponent> getGroup() { 
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          return this.group;
        }

        public boolean hasGroup() { 
          if (this.group == null)
            return false;
          for (TermComponent item : this.group)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #group} (Nested group of Contract Provisions.)
         */
    // syntactic sugar
        public TermComponent addGroup() { //3
          TermComponent t = new TermComponent();
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          this.group.add(t);
          return t;
        }

    // syntactic sugar
        public TermComponent addGroup(TermComponent t) { //3
          if (t == null)
            return this;
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          this.group.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Unique identifier for this particular Contract Provision.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("issued", "dateTime", "When this Contract Provision was issued.", 0, java.lang.Integer.MAX_VALUE, issued));
          childrenList.add(new Property("applies", "Period", "Relevant time or time-period when this Contract Provision is applicable.", 0, java.lang.Integer.MAX_VALUE, applies));
          childrenList.add(new Property("type", "CodeableConcept", "Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("subType", "CodeableConcept", "Subtype of this Contract Provision, e.g. life time maximum payment for a contract term for specific valued item, e.g. disability payment.", 0, java.lang.Integer.MAX_VALUE, subType));
          childrenList.add(new Property("topic", "Reference(Any)", "The matter under consideration about which this Contract forms an agreement under which the disposition of this matter is governed in accordance to the Contract.terms, such as privileges and delegation of privileges, rights in property such as real estate, intellectual property or a patient’s information, a relationship or partnership, or valued items such as goods and services.", 0, java.lang.Integer.MAX_VALUE, topic));
          childrenList.add(new Property("subject", "Reference(Any)", "Who or what this Contract Provision is about.", 0, java.lang.Integer.MAX_VALUE, subject));
          childrenList.add(new Property("action", "CodeableConcept", "Action stipulated by this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("actionReason", "CodeableConcept", "Reason or purpose for the action stipulated by this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, actionReason));
          childrenList.add(new Property("party", "", "Parties to the Contract, specifically, the grantor(s) and grantee(s) such as a person or an organization which is bound by the contract to the actions specified on the Contract.topic and the terms of the Contract that either extend or restrict the overall action on the topic by, for example, stipulating specific policies or obligations constraining actions and action reasons permitted or denied on a subset of the Contract topic, actors, subject(s), and extending or limiting the Contract.period of applicability or valuation of items under consideration.", 0, java.lang.Integer.MAX_VALUE, party));
          childrenList.add(new Property("text", "string", "Human readable form of this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("valuedItem", "", "Contract Provision Valued Item List.", 0, java.lang.Integer.MAX_VALUE, valuedItem));
          childrenList.add(new Property("group", "@Contract.term", "Nested group of Contract Provisions.", 0, java.lang.Integer.MAX_VALUE, group));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("issued"))
          this.issued = castToDateTime(value); // DateTimeType
        else if (name.equals("applies"))
          this.applies = castToPeriod(value); // Period
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subType"))
          this.subType = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("topic"))
          this.topic = castToReference(value); // Reference
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("action"))
          this.getAction().add(castToCodeableConcept(value));
        else if (name.equals("actionReason"))
          this.getActionReason().add(castToCodeableConcept(value));
        else if (name.equals("party"))
          this.getParty().add((TermPartyComponent) value);
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else if (name.equals("valuedItem"))
          this.getValuedItem().add((TermValuedItemComponent) value);
        else if (name.equals("group"))
          this.getGroup().add((TermComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.issued");
        }
        else if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          this.subType = new CodeableConcept();
          return this.subType;
        }
        else if (name.equals("topic")) {
          this.topic = new Reference();
          return this.topic;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("actionReason")) {
          return addActionReason();
        }
        else if (name.equals("party")) {
          return addParty();
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
        }
        else if (name.equals("valuedItem")) {
          return addValuedItem();
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else
          return super.addChild(name);
      }

      public TermComponent copy() {
        TermComponent dst = new TermComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        dst.type = type == null ? null : type.copy();
        dst.subType = subType == null ? null : subType.copy();
        dst.topic = topic == null ? null : topic.copy();
        dst.subject = subject == null ? null : subject.copy();
        if (action != null) {
          dst.action = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : action)
            dst.action.add(i.copy());
        };
        if (actionReason != null) {
          dst.actionReason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : actionReason)
            dst.actionReason.add(i.copy());
        };
        if (party != null) {
          dst.party = new ArrayList<TermPartyComponent>();
          for (TermPartyComponent i : party)
            dst.party.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        if (valuedItem != null) {
          dst.valuedItem = new ArrayList<TermValuedItemComponent>();
          for (TermValuedItemComponent i : valuedItem)
            dst.valuedItem.add(i.copy());
        };
        if (group != null) {
          dst.group = new ArrayList<TermComponent>();
          for (TermComponent i : group)
            dst.group.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TermComponent))
          return false;
        TermComponent o = (TermComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true)
           && compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true) && compareDeep(topic, o.topic, true)
           && compareDeep(subject, o.subject, true) && compareDeep(action, o.action, true) && compareDeep(actionReason, o.actionReason, true)
           && compareDeep(party, o.party, true) && compareDeep(text, o.text, true) && compareDeep(valuedItem, o.valuedItem, true)
           && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TermComponent))
          return false;
        TermComponent o = (TermComponent) other;
        return compareValues(issued, o.issued, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (issued == null || issued.isEmpty())
           && (applies == null || applies.isEmpty()) && (type == null || type.isEmpty()) && (subType == null || subType.isEmpty())
           && (topic == null || topic.isEmpty()) && (subject == null || subject.isEmpty()) && (action == null || action.isEmpty())
           && (actionReason == null || actionReason.isEmpty()) && (party == null || party.isEmpty())
           && (text == null || text.isEmpty()) && (valuedItem == null || valuedItem.isEmpty()) && (group == null || group.isEmpty())
          ;
      }

  public String fhirType() {
    return "Contract.term";

  }

  }

    @Block()
    public static class TermPartyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The party assigned a role in this Contract Provision.
         */
        @Child(name = "entity", type = {Contract.class, Device.class, Group.class, Location.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class, Substance.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Party List", formalDefinition="The party assigned a role in this Contract Provision." )
        protected Reference entity;

        /**
         * The actual object that is the target of the reference (The party assigned a role in this Contract Provision.)
         */
        protected Resource entityTarget;

        /**
         * Role played by the party assigned this role in the execution of this Contract Provision.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Party Role", formalDefinition="Role played by the party assigned this role in the execution of this Contract Provision." )
        protected List<CodeableConcept> role;

        private static final long serialVersionUID = 1371245689L;

    /**
     * Constructor
     */
      public TermPartyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TermPartyComponent(Reference entity) {
        super();
        this.entity = entity;
      }

        /**
         * @return {@link #entity} (The party assigned a role in this Contract Provision.)
         */
        public Reference getEntity() { 
          if (this.entity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermPartyComponent.entity");
            else if (Configuration.doAutoCreate())
              this.entity = new Reference(); // cc
          return this.entity;
        }

        public boolean hasEntity() { 
          return this.entity != null && !this.entity.isEmpty();
        }

        /**
         * @param value {@link #entity} (The party assigned a role in this Contract Provision.)
         */
        public TermPartyComponent setEntity(Reference value) { 
          this.entity = value;
          return this;
        }

        /**
         * @return {@link #entity} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party assigned a role in this Contract Provision.)
         */
        public Resource getEntityTarget() { 
          return this.entityTarget;
        }

        /**
         * @param value {@link #entity} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party assigned a role in this Contract Provision.)
         */
        public TermPartyComponent setEntityTarget(Resource value) { 
          this.entityTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (Role played by the party assigned this role in the execution of this Contract Provision.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        public boolean hasRole() { 
          if (this.role == null)
            return false;
          for (CodeableConcept item : this.role)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #role} (Role played by the party assigned this role in the execution of this Contract Provision.)
         */
    // syntactic sugar
        public CodeableConcept addRole() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return t;
        }

    // syntactic sugar
        public TermPartyComponent addRole(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("entity", "Reference(Contract|Device|Group|Location|Organization|Patient|Practitioner|RelatedPerson|Substance)", "The party assigned a role in this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, entity));
          childrenList.add(new Property("role", "CodeableConcept", "Role played by the party assigned this role in the execution of this Contract Provision.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("entity"))
          this.entity = castToReference(value); // Reference
        else if (name.equals("role"))
          this.getRole().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("entity")) {
          this.entity = new Reference();
          return this.entity;
        }
        else if (name.equals("role")) {
          return addRole();
        }
        else
          return super.addChild(name);
      }

      public TermPartyComponent copy() {
        TermPartyComponent dst = new TermPartyComponent();
        copyValues(dst);
        dst.entity = entity == null ? null : entity.copy();
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TermPartyComponent))
          return false;
        TermPartyComponent o = (TermPartyComponent) other;
        return compareDeep(entity, o.entity, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TermPartyComponent))
          return false;
        TermPartyComponent o = (TermPartyComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (entity == null || entity.isEmpty()) && (role == null || role.isEmpty())
          ;
      }

  public String fhirType() {
    return "Contract.term.party";

  }

  }

    @Block()
    public static class TermValuedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specific type of Contract Provision Valued Item that may be priced.
         */
        @Child(name = "entity", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item Type", formalDefinition="Specific type of Contract Provision Valued Item that may be priced." )
        protected Type entity;

        /**
         * Identifies a Contract Provision Valued Item instance.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item Identifier", formalDefinition="Identifies a Contract Provision Valued Item instance." )
        protected Identifier identifier;

        /**
         * Indicates the time during which this Contract Term ValuedItem information is effective.
         */
        @Child(name = "effectiveTime", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item Effective Tiem", formalDefinition="Indicates the time during which this Contract Term ValuedItem information is effective." )
        protected DateTimeType effectiveTime;

        /**
         * Specifies the units by which the Contract Provision Valued Item is measured or counted, and quantifies the countable or measurable Contract Term Valued Item instances.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item Count", formalDefinition="Specifies the units by which the Contract Provision Valued Item is measured or counted, and quantifies the countable or measurable Contract Term Valued Item instances." )
        protected SimpleQuantity quantity;

        /**
         * A Contract Provision Valued Item unit valuation measure.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item fee, charge, or cost", formalDefinition="A Contract Provision Valued Item unit valuation measure." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item Price Scaling Factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Valued Item Difficulty Scaling Factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * Expresses the product of the Contract Provision Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total Contract Term Valued Item Value", formalDefinition="Expresses the product of the Contract Provision Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        private static final long serialVersionUID = 1782449516L;

    /**
     * Constructor
     */
      public TermValuedItemComponent() {
        super();
      }

        /**
         * @return {@link #entity} (Specific type of Contract Provision Valued Item that may be priced.)
         */
        public Type getEntity() { 
          return this.entity;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Provision Valued Item that may be priced.)
         */
        public CodeableConcept getEntityCodeableConcept() throws FHIRException { 
          if (!(this.entity instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (CodeableConcept) this.entity;
        }

        public boolean hasEntityCodeableConcept() { 
          return this.entity instanceof CodeableConcept;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Provision Valued Item that may be priced.)
         */
        public Reference getEntityReference() throws FHIRException { 
          if (!(this.entity instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (Reference) this.entity;
        }

        public boolean hasEntityReference() { 
          return this.entity instanceof Reference;
        }

        public boolean hasEntity() { 
          return this.entity != null && !this.entity.isEmpty();
        }

        /**
         * @param value {@link #entity} (Specific type of Contract Provision Valued Item that may be priced.)
         */
        public TermValuedItemComponent setEntity(Type value) { 
          this.entity = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Identifies a Contract Provision Valued Item instance.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermValuedItemComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifies a Contract Provision Valued Item instance.)
         */
        public TermValuedItemComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #effectiveTime} (Indicates the time during which this Contract Term ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public DateTimeType getEffectiveTimeElement() { 
          if (this.effectiveTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermValuedItemComponent.effectiveTime");
            else if (Configuration.doAutoCreate())
              this.effectiveTime = new DateTimeType(); // bb
          return this.effectiveTime;
        }

        public boolean hasEffectiveTimeElement() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        public boolean hasEffectiveTime() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        /**
         * @param value {@link #effectiveTime} (Indicates the time during which this Contract Term ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public TermValuedItemComponent setEffectiveTimeElement(DateTimeType value) { 
          this.effectiveTime = value;
          return this;
        }

        /**
         * @return Indicates the time during which this Contract Term ValuedItem information is effective.
         */
        public Date getEffectiveTime() { 
          return this.effectiveTime == null ? null : this.effectiveTime.getValue();
        }

        /**
         * @param value Indicates the time during which this Contract Term ValuedItem information is effective.
         */
        public TermValuedItemComponent setEffectiveTime(Date value) { 
          if (value == null)
            this.effectiveTime = null;
          else {
            if (this.effectiveTime == null)
              this.effectiveTime = new DateTimeType();
            this.effectiveTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #quantity} (Specifies the units by which the Contract Provision Valued Item is measured or counted, and quantifies the countable or measurable Contract Term Valued Item instances.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermValuedItemComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Specifies the units by which the Contract Provision Valued Item is measured or counted, and quantifies the countable or measurable Contract Term Valued Item instances.)
         */
        public TermValuedItemComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (A Contract Provision Valued Item unit valuation measure.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermValuedItemComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (A Contract Provision Valued Item unit valuation measure.)
         */
        public TermValuedItemComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermValuedItemComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public TermValuedItemComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public TermValuedItemComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public TermValuedItemComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public TermValuedItemComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermValuedItemComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType(); // bb
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public TermValuedItemComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.
         */
        public TermValuedItemComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.
         */
        public TermValuedItemComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.
         */
        public TermValuedItemComponent setPoints(double value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (Expresses the product of the Contract Provision Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermValuedItemComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (Expresses the product of the Contract Provision Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public TermValuedItemComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Provision Valued Item that may be priced.", 0, java.lang.Integer.MAX_VALUE, entity));
          childrenList.add(new Property("identifier", "Identifier", "Identifies a Contract Provision Valued Item instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("effectiveTime", "dateTime", "Indicates the time during which this Contract Term ValuedItem information is effective.", 0, java.lang.Integer.MAX_VALUE, effectiveTime));
          childrenList.add(new Property("quantity", "SimpleQuantity", "Specifies the units by which the Contract Provision Valued Item is measured or counted, and quantifies the countable or measurable Contract Term Valued Item instances.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "A Contract Provision Valued Item unit valuation measure.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of the Contract Provision Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Provision Valued Item delivered. The concept of Points allows for assignment of point values for a Contract ProvisionValued Item, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "Expresses the product of the Contract Provision Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("entity[x]"))
          this.entity = (Type) value; // Type
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("effectiveTime"))
          this.effectiveTime = castToDateTime(value); // DateTimeType
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("unitPrice"))
          this.unitPrice = castToMoney(value); // Money
        else if (name.equals("factor"))
          this.factor = castToDecimal(value); // DecimalType
        else if (name.equals("points"))
          this.points = castToDecimal(value); // DecimalType
        else if (name.equals("net"))
          this.net = castToMoney(value); // Money
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("entityCodeableConcept")) {
          this.entity = new CodeableConcept();
          return this.entity;
        }
        else if (name.equals("entityReference")) {
          this.entity = new Reference();
          return this.entity;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("effectiveTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.effectiveTime");
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else
          return super.addChild(name);
      }

      public TermValuedItemComponent copy() {
        TermValuedItemComponent dst = new TermValuedItemComponent();
        copyValues(dst);
        dst.entity = entity == null ? null : entity.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.effectiveTime = effectiveTime == null ? null : effectiveTime.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TermValuedItemComponent))
          return false;
        TermValuedItemComponent o = (TermValuedItemComponent) other;
        return compareDeep(entity, o.entity, true) && compareDeep(identifier, o.identifier, true) && compareDeep(effectiveTime, o.effectiveTime, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TermValuedItemComponent))
          return false;
        TermValuedItemComponent o = (TermValuedItemComponent) other;
        return compareValues(effectiveTime, o.effectiveTime, true) && compareValues(factor, o.factor, true)
           && compareValues(points, o.points, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (entity == null || entity.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (effectiveTime == null || effectiveTime.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (unitPrice == null || unitPrice.isEmpty()) && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty())
           && (net == null || net.isEmpty());
      }

  public String fhirType() {
    return "Contract.term.valuedItem";

  }

  }

    @Block()
    public static class FriendlyLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.
         */
        @Child(name = "content", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Easily comprehended representation of this Contract", formalDefinition="Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public FriendlyLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public FriendlyLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public FriendlyLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]"))
          this.content = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public FriendlyLanguageComponent copy() {
        FriendlyLanguageComponent dst = new FriendlyLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (content == null || content.isEmpty());
      }

  public String fhirType() {
    return "Contract.friendly";

  }

  }

    @Block()
    public static class LegalLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Contract legal text in human renderable form.
         */
        @Child(name = "content", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Legal Text", formalDefinition="Contract legal text in human renderable form." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public LegalLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LegalLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Contract legal text in human renderable form.)
         */
        public LegalLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]"))
          this.content = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public LegalLanguageComponent copy() {
        LegalLanguageComponent dst = new LegalLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (content == null || content.isEmpty());
      }

  public String fhirType() {
    return "Contract.legal";

  }

  }

    @Block()
    public static class ComputableLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).
         */
        @Child(name = "content", type = {Attachment.class, DocumentReference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Computable Contract Rules", formalDefinition="Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal)." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public ComputableLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ComputableLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public ComputableLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, java.lang.Integer.MAX_VALUE, content));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]"))
          this.content = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public ComputableLanguageComponent copy() {
        ComputableLanguageComponent dst = new ComputableLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ComputableLanguageComponent))
          return false;
        ComputableLanguageComponent o = (ComputableLanguageComponent) other;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ComputableLanguageComponent))
          return false;
        ComputableLanguageComponent o = (ComputableLanguageComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (content == null || content.isEmpty());
      }

  public String fhirType() {
    return "Contract.rule";

  }

  }

    /**
     * Unique identifier for this Contract.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Contract identifier", formalDefinition="Unique identifier for this Contract." )
    protected Identifier identifier;

    /**
     * When this  Contract was issued.
     */
    @Child(name = "issued", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When this Contract was issued", formalDefinition="When this  Contract was issued." )
    protected DateTimeType issued;

    /**
     * Relevant time or time-period when this Contract is applicable.
     */
    @Child(name = "applies", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Effective time", formalDefinition="Relevant time or time-period when this Contract is applicable." )
    protected Period applies;

    /**
     * Who and/or what this Contract is about: typically a Patient, Organization, or valued items such as goods and services.
     */
    @Child(name = "subject", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Subject of this Contract", formalDefinition="Who and/or what this Contract is about: typically a Patient, Organization, or valued items such as goods and services." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (Who and/or what this Contract is about: typically a Patient, Organization, or valued items such as goods and services.)
     */
    protected List<Resource> subjectTarget;


    /**
     * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.
     */
    @Child(name = "authority", type = {Organization.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Authority under which this Contract has standing", formalDefinition="A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies." )
    protected List<Reference> authority;
    /**
     * The actual objects that are the target of the reference (A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    protected List<Organization> authorityTarget;


    /**
     * Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.
     */
    @Child(name = "domain", type = {Location.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Domain in which this Contract applies", formalDefinition="Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources." )
    protected List<Reference> domain;
    /**
     * The actual objects that are the target of the reference (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    protected List<Location> domainTarget;


    /**
     * Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Contract Tyoe", formalDefinition="Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc." )
    protected CodeableConcept type;

    /**
     * More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.
     */
    @Child(name = "subType", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contract Subtype", formalDefinition="More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent." )
    protected List<CodeableConcept> subType;

    /**
     * Action stipulated by this Contract.
     */
    @Child(name = "action", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Action", formalDefinition="Action stipulated by this Contract." )
    protected List<CodeableConcept> action;

    /**
     * Reason for action stipulated by this Contract.
     */
    @Child(name = "actionReason", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Action Reason", formalDefinition="Reason for action stipulated by this Contract." )
    protected List<CodeableConcept> actionReason;

    /**
     * List of parties to the Contract.
     */
    @Child(name = "party", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Party", formalDefinition="List of parties to the Contract." )
    protected List<PartyComponent> party;

    /**
     * Contract Valued Item List.
     */
    @Child(name = "valuedItem", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Valued Item", formalDefinition="Contract Valued Item List." )
    protected List<ValuedItemComponent> valuedItem;

    /**
     * Party signing this Contract.
     */
    @Child(name = "signer", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Signer", formalDefinition="Party signing this Contract." )
    protected List<SignatoryComponent> signer;

    /**
     * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
     */
    @Child(name = "term", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Term List", formalDefinition="One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups." )
    protected List<TermComponent> term;

    /**
     * Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.
     */
    @Child(name = "binding", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Binding Contract", formalDefinition="Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract." )
    protected Type binding;

    /**
     * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     */
    @Child(name = "friendly", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Friendly Language", formalDefinition="The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement." )
    protected List<FriendlyLanguageComponent> friendly;

    /**
     * List of Legal expressions or representations of this Contract.
     */
    @Child(name = "legal", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Legal Language", formalDefinition="List of Legal expressions or representations of this Contract." )
    protected List<LegalLanguageComponent> legal;

    /**
     * List of Computable Policy Rule Language Representations of this Contract.
     */
    @Child(name = "rule", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Computable Contract Language", formalDefinition="List of Computable Policy Rule Language Representations of this Contract." )
    protected List<ComputableLanguageComponent> rule;

    private static final long serialVersionUID = -2064861271L;

  /**
   * Constructor
   */
    public Contract() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique identifier for this Contract.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Unique identifier for this Contract.)
     */
    public Contract setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #issued} (When this  Contract was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DateTimeType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new DateTimeType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (When this  Contract was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Contract setIssuedElement(DateTimeType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return When this  Contract was issued.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value When this  Contract was issued.
     */
    public Contract setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new DateTimeType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #applies} (Relevant time or time-period when this Contract is applicable.)
     */
    public Period getApplies() { 
      if (this.applies == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.applies");
        else if (Configuration.doAutoCreate())
          this.applies = new Period(); // cc
      return this.applies;
    }

    public boolean hasApplies() { 
      return this.applies != null && !this.applies.isEmpty();
    }

    /**
     * @param value {@link #applies} (Relevant time or time-period when this Contract is applicable.)
     */
    public Contract setApplies(Period value) { 
      this.applies = value;
      return this;
    }

    /**
     * @return {@link #subject} (Who and/or what this Contract is about: typically a Patient, Organization, or valued items such as goods and services.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #subject} (Who and/or what this Contract is about: typically a Patient, Organization, or valued items such as goods and services.)
     */
    // syntactic sugar
    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addSubject(Reference t) { //3
      if (t == null)
        return this;
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return this;
    }

    /**
     * @return {@link #subject} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Who and/or what this Contract is about: typically a Patient, Organization, or valued items such as goods and services.)
     */
    public List<Resource> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<Resource>();
      return this.subjectTarget;
    }

    /**
     * @return {@link #authority} (A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    public List<Reference> getAuthority() { 
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      return this.authority;
    }

    public boolean hasAuthority() { 
      if (this.authority == null)
        return false;
      for (Reference item : this.authority)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #authority} (A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    // syntactic sugar
    public Reference addAuthority() { //3
      Reference t = new Reference();
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addAuthority(Reference t) { //3
      if (t == null)
        return this;
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return this;
    }

    /**
     * @return {@link #authority} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    public List<Organization> getAuthorityTarget() { 
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      return this.authorityTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #authority} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    public Organization addAuthorityTarget() { 
      Organization r = new Organization();
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      this.authorityTarget.add(r);
      return r;
    }

    /**
     * @return {@link #domain} (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    public List<Reference> getDomain() { 
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      return this.domain;
    }

    public boolean hasDomain() { 
      if (this.domain == null)
        return false;
      for (Reference item : this.domain)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #domain} (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    // syntactic sugar
    public Reference addDomain() { //3
      Reference t = new Reference();
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addDomain(Reference t) { //3
      if (t == null)
        return this;
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return this;
    }

    /**
     * @return {@link #domain} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    public List<Location> getDomainTarget() { 
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      return this.domainTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #domain} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    public Location addDomainTarget() { 
      Location r = new Location();
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      this.domainTarget.add(r);
      return r;
    }

    /**
     * @return {@link #type} (Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.)
     */
    public Contract setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subType} (More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.)
     */
    public List<CodeableConcept> getSubType() { 
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      return this.subType;
    }

    public boolean hasSubType() { 
      if (this.subType == null)
        return false;
      for (CodeableConcept item : this.subType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #subType} (More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.)
     */
    // syntactic sugar
    public CodeableConcept addSubType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addSubType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return this;
    }

    /**
     * @return {@link #action} (Action stipulated by this Contract.)
     */
    public List<CodeableConcept> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<CodeableConcept>();
      return this.action;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (CodeableConcept item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #action} (Action stipulated by this Contract.)
     */
    // syntactic sugar
    public CodeableConcept addAction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.action == null)
        this.action = new ArrayList<CodeableConcept>();
      this.action.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addAction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<CodeableConcept>();
      this.action.add(t);
      return this;
    }

    /**
     * @return {@link #actionReason} (Reason for action stipulated by this Contract.)
     */
    public List<CodeableConcept> getActionReason() { 
      if (this.actionReason == null)
        this.actionReason = new ArrayList<CodeableConcept>();
      return this.actionReason;
    }

    public boolean hasActionReason() { 
      if (this.actionReason == null)
        return false;
      for (CodeableConcept item : this.actionReason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #actionReason} (Reason for action stipulated by this Contract.)
     */
    // syntactic sugar
    public CodeableConcept addActionReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.actionReason == null)
        this.actionReason = new ArrayList<CodeableConcept>();
      this.actionReason.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addActionReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.actionReason == null)
        this.actionReason = new ArrayList<CodeableConcept>();
      this.actionReason.add(t);
      return this;
    }

    /**
     * @return {@link #party} (List of parties to the Contract.)
     */
    public List<PartyComponent> getParty() { 
      if (this.party == null)
        this.party = new ArrayList<PartyComponent>();
      return this.party;
    }

    public boolean hasParty() { 
      if (this.party == null)
        return false;
      for (PartyComponent item : this.party)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #party} (List of parties to the Contract.)
     */
    // syntactic sugar
    public PartyComponent addParty() { //3
      PartyComponent t = new PartyComponent();
      if (this.party == null)
        this.party = new ArrayList<PartyComponent>();
      this.party.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addParty(PartyComponent t) { //3
      if (t == null)
        return this;
      if (this.party == null)
        this.party = new ArrayList<PartyComponent>();
      this.party.add(t);
      return this;
    }

    /**
     * @return {@link #valuedItem} (Contract Valued Item List.)
     */
    public List<ValuedItemComponent> getValuedItem() { 
      if (this.valuedItem == null)
        this.valuedItem = new ArrayList<ValuedItemComponent>();
      return this.valuedItem;
    }

    public boolean hasValuedItem() { 
      if (this.valuedItem == null)
        return false;
      for (ValuedItemComponent item : this.valuedItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #valuedItem} (Contract Valued Item List.)
     */
    // syntactic sugar
    public ValuedItemComponent addValuedItem() { //3
      ValuedItemComponent t = new ValuedItemComponent();
      if (this.valuedItem == null)
        this.valuedItem = new ArrayList<ValuedItemComponent>();
      this.valuedItem.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addValuedItem(ValuedItemComponent t) { //3
      if (t == null)
        return this;
      if (this.valuedItem == null)
        this.valuedItem = new ArrayList<ValuedItemComponent>();
      this.valuedItem.add(t);
      return this;
    }

    /**
     * @return {@link #signer} (Party signing this Contract.)
     */
    public List<SignatoryComponent> getSigner() { 
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      return this.signer;
    }

    public boolean hasSigner() { 
      if (this.signer == null)
        return false;
      for (SignatoryComponent item : this.signer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #signer} (Party signing this Contract.)
     */
    // syntactic sugar
    public SignatoryComponent addSigner() { //3
      SignatoryComponent t = new SignatoryComponent();
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      this.signer.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addSigner(SignatoryComponent t) { //3
      if (t == null)
        return this;
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      this.signer.add(t);
      return this;
    }

    /**
     * @return {@link #term} (One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.)
     */
    public List<TermComponent> getTerm() { 
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      return this.term;
    }

    public boolean hasTerm() { 
      if (this.term == null)
        return false;
      for (TermComponent item : this.term)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #term} (One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.)
     */
    // syntactic sugar
    public TermComponent addTerm() { //3
      TermComponent t = new TermComponent();
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      this.term.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addTerm(TermComponent t) { //3
      if (t == null)
        return this;
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      this.term.add(t);
      return this;
    }

    /**
     * @return {@link #binding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Type getBinding() { 
      return this.binding;
    }

    /**
     * @return {@link #binding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Attachment getBindingAttachment() throws FHIRException { 
      if (!(this.binding instanceof Attachment))
        throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.binding.getClass().getName()+" was encountered");
      return (Attachment) this.binding;
    }

    public boolean hasBindingAttachment() { 
      return this.binding instanceof Attachment;
    }

    /**
     * @return {@link #binding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Reference getBindingReference() throws FHIRException { 
      if (!(this.binding instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.binding.getClass().getName()+" was encountered");
      return (Reference) this.binding;
    }

    public boolean hasBindingReference() { 
      return this.binding instanceof Reference;
    }

    public boolean hasBinding() { 
      return this.binding != null && !this.binding.isEmpty();
    }

    /**
     * @param value {@link #binding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Contract setBinding(Type value) { 
      this.binding = value;
      return this;
    }

    /**
     * @return {@link #friendly} (The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.)
     */
    public List<FriendlyLanguageComponent> getFriendly() { 
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      return this.friendly;
    }

    public boolean hasFriendly() { 
      if (this.friendly == null)
        return false;
      for (FriendlyLanguageComponent item : this.friendly)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #friendly} (The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.)
     */
    // syntactic sugar
    public FriendlyLanguageComponent addFriendly() { //3
      FriendlyLanguageComponent t = new FriendlyLanguageComponent();
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addFriendly(FriendlyLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return this;
    }

    /**
     * @return {@link #legal} (List of Legal expressions or representations of this Contract.)
     */
    public List<LegalLanguageComponent> getLegal() { 
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      return this.legal;
    }

    public boolean hasLegal() { 
      if (this.legal == null)
        return false;
      for (LegalLanguageComponent item : this.legal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #legal} (List of Legal expressions or representations of this Contract.)
     */
    // syntactic sugar
    public LegalLanguageComponent addLegal() { //3
      LegalLanguageComponent t = new LegalLanguageComponent();
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addLegal(LegalLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return this;
    }

    /**
     * @return {@link #rule} (List of Computable Policy Rule Language Representations of this Contract.)
     */
    public List<ComputableLanguageComponent> getRule() { 
      if (this.rule == null)
        this.rule = new ArrayList<ComputableLanguageComponent>();
      return this.rule;
    }

    public boolean hasRule() { 
      if (this.rule == null)
        return false;
      for (ComputableLanguageComponent item : this.rule)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #rule} (List of Computable Policy Rule Language Representations of this Contract.)
     */
    // syntactic sugar
    public ComputableLanguageComponent addRule() { //3
      ComputableLanguageComponent t = new ComputableLanguageComponent();
      if (this.rule == null)
        this.rule = new ArrayList<ComputableLanguageComponent>();
      this.rule.add(t);
      return t;
    }

    // syntactic sugar
    public Contract addRule(ComputableLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.rule == null)
        this.rule = new ArrayList<ComputableLanguageComponent>();
      this.rule.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier for this Contract.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("issued", "dateTime", "When this  Contract was issued.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("applies", "Period", "Relevant time or time-period when this Contract is applicable.", 0, java.lang.Integer.MAX_VALUE, applies));
        childrenList.add(new Property("subject", "Reference(Any)", "Who and/or what this Contract is about: typically a Patient, Organization, or valued items such as goods and services.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("authority", "Reference(Organization)", "A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.", 0, java.lang.Integer.MAX_VALUE, authority));
        childrenList.add(new Property("domain", "Reference(Location)", "Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.", 0, java.lang.Integer.MAX_VALUE, domain));
        childrenList.add(new Property("type", "CodeableConcept", "Type of Contract such as an insurance policy, real estate contract, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subType", "CodeableConcept", "More specific type or specialization of an overarching or more general contract such as auto insurance, home owner  insurance, prenupial agreement, Advanced-Directive, or privacy consent.", 0, java.lang.Integer.MAX_VALUE, subType));
        childrenList.add(new Property("action", "CodeableConcept", "Action stipulated by this Contract.", 0, java.lang.Integer.MAX_VALUE, action));
        childrenList.add(new Property("actionReason", "CodeableConcept", "Reason for action stipulated by this Contract.", 0, java.lang.Integer.MAX_VALUE, actionReason));
        childrenList.add(new Property("party", "", "List of parties to the Contract.", 0, java.lang.Integer.MAX_VALUE, party));
        childrenList.add(new Property("valuedItem", "", "Contract Valued Item List.", 0, java.lang.Integer.MAX_VALUE, valuedItem));
        childrenList.add(new Property("signer", "", "Party signing this Contract.", 0, java.lang.Integer.MAX_VALUE, signer));
        childrenList.add(new Property("term", "", "One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.", 0, java.lang.Integer.MAX_VALUE, term));
        childrenList.add(new Property("binding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, java.lang.Integer.MAX_VALUE, binding));
        childrenList.add(new Property("friendly", "", "The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.", 0, java.lang.Integer.MAX_VALUE, friendly));
        childrenList.add(new Property("legal", "", "List of Legal expressions or representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, legal));
        childrenList.add(new Property("rule", "", "List of Computable Policy Rule Language Representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, rule));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("issued"))
          this.issued = castToDateTime(value); // DateTimeType
        else if (name.equals("applies"))
          this.applies = castToPeriod(value); // Period
        else if (name.equals("subject"))
          this.getSubject().add(castToReference(value));
        else if (name.equals("authority"))
          this.getAuthority().add(castToReference(value));
        else if (name.equals("domain"))
          this.getDomain().add(castToReference(value));
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subType"))
          this.getSubType().add(castToCodeableConcept(value));
        else if (name.equals("action"))
          this.getAction().add(castToCodeableConcept(value));
        else if (name.equals("actionReason"))
          this.getActionReason().add(castToCodeableConcept(value));
        else if (name.equals("party"))
          this.getParty().add((PartyComponent) value);
        else if (name.equals("valuedItem"))
          this.getValuedItem().add((ValuedItemComponent) value);
        else if (name.equals("signer"))
          this.getSigner().add((SignatoryComponent) value);
        else if (name.equals("term"))
          this.getTerm().add((TermComponent) value);
        else if (name.equals("binding[x]"))
          this.binding = (Type) value; // Type
        else if (name.equals("friendly"))
          this.getFriendly().add((FriendlyLanguageComponent) value);
        else if (name.equals("legal"))
          this.getLegal().add((LegalLanguageComponent) value);
        else if (name.equals("rule"))
          this.getRule().add((ComputableLanguageComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.issued");
        }
        else if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("authority")) {
          return addAuthority();
        }
        else if (name.equals("domain")) {
          return addDomain();
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          return addSubType();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("actionReason")) {
          return addActionReason();
        }
        else if (name.equals("party")) {
          return addParty();
        }
        else if (name.equals("valuedItem")) {
          return addValuedItem();
        }
        else if (name.equals("signer")) {
          return addSigner();
        }
        else if (name.equals("term")) {
          return addTerm();
        }
        else if (name.equals("bindingAttachment")) {
          this.binding = new Attachment();
          return this.binding;
        }
        else if (name.equals("bindingReference")) {
          this.binding = new Reference();
          return this.binding;
        }
        else if (name.equals("friendly")) {
          return addFriendly();
        }
        else if (name.equals("legal")) {
          return addLegal();
        }
        else if (name.equals("rule")) {
          return addRule();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Contract";

  }

      public Contract copy() {
        Contract dst = new Contract();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        if (authority != null) {
          dst.authority = new ArrayList<Reference>();
          for (Reference i : authority)
            dst.authority.add(i.copy());
        };
        if (domain != null) {
          dst.domain = new ArrayList<Reference>();
          for (Reference i : domain)
            dst.domain.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (subType != null) {
          dst.subType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subType)
            dst.subType.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : action)
            dst.action.add(i.copy());
        };
        if (actionReason != null) {
          dst.actionReason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : actionReason)
            dst.actionReason.add(i.copy());
        };
        if (party != null) {
          dst.party = new ArrayList<PartyComponent>();
          for (PartyComponent i : party)
            dst.party.add(i.copy());
        };
        if (valuedItem != null) {
          dst.valuedItem = new ArrayList<ValuedItemComponent>();
          for (ValuedItemComponent i : valuedItem)
            dst.valuedItem.add(i.copy());
        };
        if (signer != null) {
          dst.signer = new ArrayList<SignatoryComponent>();
          for (SignatoryComponent i : signer)
            dst.signer.add(i.copy());
        };
        if (term != null) {
          dst.term = new ArrayList<TermComponent>();
          for (TermComponent i : term)
            dst.term.add(i.copy());
        };
        dst.binding = binding == null ? null : binding.copy();
        if (friendly != null) {
          dst.friendly = new ArrayList<FriendlyLanguageComponent>();
          for (FriendlyLanguageComponent i : friendly)
            dst.friendly.add(i.copy());
        };
        if (legal != null) {
          dst.legal = new ArrayList<LegalLanguageComponent>();
          for (LegalLanguageComponent i : legal)
            dst.legal.add(i.copy());
        };
        if (rule != null) {
          dst.rule = new ArrayList<ComputableLanguageComponent>();
          for (ComputableLanguageComponent i : rule)
            dst.rule.add(i.copy());
        };
        return dst;
      }

      protected Contract typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Contract))
          return false;
        Contract o = (Contract) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true)
           && compareDeep(subject, o.subject, true) && compareDeep(authority, o.authority, true) && compareDeep(domain, o.domain, true)
           && compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true) && compareDeep(action, o.action, true)
           && compareDeep(actionReason, o.actionReason, true) && compareDeep(party, o.party, true) && compareDeep(valuedItem, o.valuedItem, true)
           && compareDeep(signer, o.signer, true) && compareDeep(term, o.term, true) && compareDeep(binding, o.binding, true)
           && compareDeep(friendly, o.friendly, true) && compareDeep(legal, o.legal, true) && compareDeep(rule, o.rule, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Contract))
          return false;
        Contract o = (Contract) other;
        return compareValues(issued, o.issued, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (issued == null || issued.isEmpty())
           && (applies == null || applies.isEmpty()) && (subject == null || subject.isEmpty()) && (authority == null || authority.isEmpty())
           && (domain == null || domain.isEmpty()) && (type == null || type.isEmpty()) && (subType == null || subType.isEmpty())
           && (action == null || action.isEmpty()) && (actionReason == null || actionReason.isEmpty())
           && (party == null || party.isEmpty()) && (valuedItem == null || valuedItem.isEmpty()) && (signer == null || signer.isEmpty())
           && (term == null || term.isEmpty()) && (binding == null || binding.isEmpty()) && (friendly == null || friendly.isEmpty())
           && (legal == null || legal.isEmpty()) && (rule == null || rule.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Contract;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identity of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Contract.identifier", description="The identity of the contract", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identity of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identity of the target of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Contract.subject", description="The identity of the target of the contract", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identity of the target of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Contract:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of the target of the contract (if a patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Contract.subject", description="The identity of the target of the contract (if a patient)", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of the target of the contract (if a patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Contract:patient").toLocked();

 /**
   * Search parameter: <b>party</b>
   * <p>
   * Description: <b>Party to the Contact</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.party.entity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="party", path="Contract.party.entity", description="Party to the Contact", type="reference" )
  public static final String SP_PARTY = "party";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>party</b>
   * <p>
   * Description: <b>Party to the Contact</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.party.entity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:party</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTY = new ca.uhn.fhir.model.api.Include("Contract:party").toLocked();

 /**
   * Search parameter: <b>signer</b>
   * <p>
   * Description: <b>Contract Signatory Party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.signer.party</b><br>
   * </p>
   */
  @SearchParamDefinition(name="signer", path="Contract.signer.party", description="Contract Signatory Party", type="reference" )
  public static final String SP_SIGNER = "signer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>signer</b>
   * <p>
   * Description: <b>Contract Signatory Party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.signer.party</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SIGNER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SIGNER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:signer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SIGNER = new ca.uhn.fhir.model.api.Include("Contract:signer").toLocked();


}

