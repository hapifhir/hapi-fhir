package org.hl7.fhir.instance.model;

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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * An authorization for the supply of glasses and/or contact lenses to a patient.
 */
@ResourceDef(name="VisionPrescription", profile="http://hl7.org/fhir/Profile/VisionPrescription")
public class VisionPrescription extends DomainResource {

    public enum EyeCodes {
        /**
         * right eye.
         */
        RIGHT, 
        /**
         * left eye.
         */
        LEFT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EyeCodes fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("right".equals(codeString))
          return RIGHT;
        if ("left".equals(codeString))
          return LEFT;
        throw new Exception("Unknown EyeCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RIGHT: return "right";
            case LEFT: return "left";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case RIGHT: return "";
            case LEFT: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case RIGHT: return "right eye.";
            case LEFT: return "left eye.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RIGHT: return "right";
            case LEFT: return "left";
            default: return "?";
          }
        }
    }

  public static class EyeCodesEnumFactory implements EnumFactory<EyeCodes> {
    public EyeCodes fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("right".equals(codeString))
          return EyeCodes.RIGHT;
        if ("left".equals(codeString))
          return EyeCodes.LEFT;
        throw new IllegalArgumentException("Unknown EyeCodes code '"+codeString+"'");
        }
    public String toCode(EyeCodes code) {
      if (code == EyeCodes.RIGHT)
        return "right";
      if (code == EyeCodes.LEFT)
        return "left";
      return "?";
      }
    }

    public enum BaseCodes {
        /**
         * top.
         */
        UP, 
        /**
         * bottom.
         */
        DOWN, 
        /**
         * inner edge.
         */
        IN, 
        /**
         * outer edge.
         */
        OUT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BaseCodes fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("up".equals(codeString))
          return UP;
        if ("down".equals(codeString))
          return DOWN;
        if ("in".equals(codeString))
          return IN;
        if ("out".equals(codeString))
          return OUT;
        throw new Exception("Unknown BaseCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UP: return "up";
            case DOWN: return "down";
            case IN: return "in";
            case OUT: return "out";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UP: return "";
            case DOWN: return "";
            case IN: return "";
            case OUT: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UP: return "top.";
            case DOWN: return "bottom.";
            case IN: return "inner edge.";
            case OUT: return "outer edge.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UP: return "up";
            case DOWN: return "down";
            case IN: return "in";
            case OUT: return "out";
            default: return "?";
          }
        }
    }

  public static class BaseCodesEnumFactory implements EnumFactory<BaseCodes> {
    public BaseCodes fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("up".equals(codeString))
          return BaseCodes.UP;
        if ("down".equals(codeString))
          return BaseCodes.DOWN;
        if ("in".equals(codeString))
          return BaseCodes.IN;
        if ("out".equals(codeString))
          return BaseCodes.OUT;
        throw new IllegalArgumentException("Unknown BaseCodes code '"+codeString+"'");
        }
    public String toCode(BaseCodes code) {
      if (code == BaseCodes.UP)
        return "up";
      if (code == BaseCodes.DOWN)
        return "down";
      if (code == BaseCodes.IN)
        return "in";
      if (code == BaseCodes.OUT)
        return "out";
      return "?";
      }
    }

    @Block()
    public static class VisionPrescriptionDispenseComponent extends BackboneElement {
        /**
         * Identifies the type of Vision correction product which is required for the patient.
         */
        @Child(name="product", type={Coding.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Product to be supplied", formalDefinition="Identifies the type of Vision correction product which is required for the patient." )
        protected Coding product;

        /**
         * The eye for which the lens applies.
         */
        @Child(name="eye", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="right | left", formalDefinition="The eye for which the lens applies." )
        protected Enumeration<EyeCodes> eye;

        /**
         * Lens power measured in diopters (0.25 units).
         */
        @Child(name="sphere", type={DecimalType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Lens sphere", formalDefinition="Lens power measured in diopters (0.25 units)." )
        protected DecimalType sphere;

        /**
         * Power adjustment for astigmatism measured in diopters (0.25 units).
         */
        @Child(name="cylinder", type={DecimalType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Lens cylinder", formalDefinition="Power adjustment for astigmatism measured in diopters (0.25 units)." )
        protected DecimalType cylinder;

        /**
         * Adjustment for astigmatism measured in integer degrees.
         */
        @Child(name="axis", type={IntegerType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Lens axis", formalDefinition="Adjustment for astigmatism measured in integer degrees." )
        protected IntegerType axis;

        /**
         * Amount of prism to compensate for eye alignment in fractional units.
         */
        @Child(name="prism", type={DecimalType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Lens prism", formalDefinition="Amount of prism to compensate for eye alignment in fractional units." )
        protected DecimalType prism;

        /**
         * The relative base, or reference lens edge, for the prism.
         */
        @Child(name="base", type={CodeType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="up | down | in | out", formalDefinition="The relative base, or reference lens edge, for the prism." )
        protected Enumeration<BaseCodes> base;

        /**
         * Power adjustment for multifocal lenses measured in diopters (0.25 units).
         */
        @Child(name="add", type={DecimalType.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Lens add", formalDefinition="Power adjustment for multifocal lenses measured in diopters (0.25 units)." )
        protected DecimalType add;

        /**
         * Contact lens power measured in diopters (0.25 units).
         */
        @Child(name="power", type={DecimalType.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Contact Lens power", formalDefinition="Contact lens power measured in diopters (0.25 units)." )
        protected DecimalType power;

        /**
         * Back Curvature measured in millimeters.
         */
        @Child(name="backCurve", type={DecimalType.class}, order=10, min=0, max=1)
        @Description(shortDefinition="Contact lens back curvature", formalDefinition="Back Curvature measured in millimeters." )
        protected DecimalType backCurve;

        /**
         * Contact lens diameter measured in millimeters.
         */
        @Child(name="diameter", type={DecimalType.class}, order=11, min=0, max=1)
        @Description(shortDefinition="Contact Lens diameter", formalDefinition="Contact lens diameter measured in millimeters." )
        protected DecimalType diameter;

        /**
         * The recommended maximum wear period for the lens.
         */
        @Child(name="duration", type={Quantity.class}, order=12, min=0, max=1)
        @Description(shortDefinition="Lens wear duration", formalDefinition="The recommended maximum wear period for the lens." )
        protected Quantity duration;

        /**
         * Special color or pattern.
         */
        @Child(name="color", type={StringType.class}, order=13, min=0, max=1)
        @Description(shortDefinition="Lens add", formalDefinition="Special color or pattern." )
        protected StringType color;

        /**
         * Brand recommendations or restrictions.
         */
        @Child(name="brand", type={StringType.class}, order=14, min=0, max=1)
        @Description(shortDefinition="Lens add", formalDefinition="Brand recommendations or restrictions." )
        protected StringType brand;

        /**
         * Notes for special requirements such as coatings and lens materials.
         */
        @Child(name="notes", type={StringType.class}, order=15, min=0, max=1)
        @Description(shortDefinition="Notes for coatings", formalDefinition="Notes for special requirements such as coatings and lens materials." )
        protected StringType notes;

        private static final long serialVersionUID = 548964753L;

      public VisionPrescriptionDispenseComponent() {
        super();
      }

      public VisionPrescriptionDispenseComponent(Coding product) {
        super();
        this.product = product;
      }

        /**
         * @return {@link #product} (Identifies the type of Vision correction product which is required for the patient.)
         */
        public Coding getProduct() { 
          if (this.product == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.product");
            else if (Configuration.doAutoCreate())
              this.product = new Coding(); // cc
          return this.product;
        }

        public boolean hasProduct() { 
          return this.product != null && !this.product.isEmpty();
        }

        /**
         * @param value {@link #product} (Identifies the type of Vision correction product which is required for the patient.)
         */
        public VisionPrescriptionDispenseComponent setProduct(Coding value) { 
          this.product = value;
          return this;
        }

        /**
         * @return {@link #eye} (The eye for which the lens applies.). This is the underlying object with id, value and extensions. The accessor "getEye" gives direct access to the value
         */
        public Enumeration<EyeCodes> getEyeElement() { 
          if (this.eye == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.eye");
            else if (Configuration.doAutoCreate())
              this.eye = new Enumeration<EyeCodes>(new EyeCodesEnumFactory()); // bb
          return this.eye;
        }

        public boolean hasEyeElement() { 
          return this.eye != null && !this.eye.isEmpty();
        }

        public boolean hasEye() { 
          return this.eye != null && !this.eye.isEmpty();
        }

        /**
         * @param value {@link #eye} (The eye for which the lens applies.). This is the underlying object with id, value and extensions. The accessor "getEye" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setEyeElement(Enumeration<EyeCodes> value) { 
          this.eye = value;
          return this;
        }

        /**
         * @return The eye for which the lens applies.
         */
        public EyeCodes getEye() { 
          return this.eye == null ? null : this.eye.getValue();
        }

        /**
         * @param value The eye for which the lens applies.
         */
        public VisionPrescriptionDispenseComponent setEye(EyeCodes value) { 
          if (value == null)
            this.eye = null;
          else {
            if (this.eye == null)
              this.eye = new Enumeration<EyeCodes>(new EyeCodesEnumFactory());
            this.eye.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #sphere} (Lens power measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getSphere" gives direct access to the value
         */
        public DecimalType getSphereElement() { 
          if (this.sphere == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.sphere");
            else if (Configuration.doAutoCreate())
              this.sphere = new DecimalType(); // bb
          return this.sphere;
        }

        public boolean hasSphereElement() { 
          return this.sphere != null && !this.sphere.isEmpty();
        }

        public boolean hasSphere() { 
          return this.sphere != null && !this.sphere.isEmpty();
        }

        /**
         * @param value {@link #sphere} (Lens power measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getSphere" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setSphereElement(DecimalType value) { 
          this.sphere = value;
          return this;
        }

        /**
         * @return Lens power measured in diopters (0.25 units).
         */
        public BigDecimal getSphere() { 
          return this.sphere == null ? null : this.sphere.getValue();
        }

        /**
         * @param value Lens power measured in diopters (0.25 units).
         */
        public VisionPrescriptionDispenseComponent setSphere(BigDecimal value) { 
          if (value == null)
            this.sphere = null;
          else {
            if (this.sphere == null)
              this.sphere = new DecimalType();
            this.sphere.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #cylinder} (Power adjustment for astigmatism measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getCylinder" gives direct access to the value
         */
        public DecimalType getCylinderElement() { 
          if (this.cylinder == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.cylinder");
            else if (Configuration.doAutoCreate())
              this.cylinder = new DecimalType(); // bb
          return this.cylinder;
        }

        public boolean hasCylinderElement() { 
          return this.cylinder != null && !this.cylinder.isEmpty();
        }

        public boolean hasCylinder() { 
          return this.cylinder != null && !this.cylinder.isEmpty();
        }

        /**
         * @param value {@link #cylinder} (Power adjustment for astigmatism measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getCylinder" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setCylinderElement(DecimalType value) { 
          this.cylinder = value;
          return this;
        }

        /**
         * @return Power adjustment for astigmatism measured in diopters (0.25 units).
         */
        public BigDecimal getCylinder() { 
          return this.cylinder == null ? null : this.cylinder.getValue();
        }

        /**
         * @param value Power adjustment for astigmatism measured in diopters (0.25 units).
         */
        public VisionPrescriptionDispenseComponent setCylinder(BigDecimal value) { 
          if (value == null)
            this.cylinder = null;
          else {
            if (this.cylinder == null)
              this.cylinder = new DecimalType();
            this.cylinder.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #axis} (Adjustment for astigmatism measured in integer degrees.). This is the underlying object with id, value and extensions. The accessor "getAxis" gives direct access to the value
         */
        public IntegerType getAxisElement() { 
          if (this.axis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.axis");
            else if (Configuration.doAutoCreate())
              this.axis = new IntegerType(); // bb
          return this.axis;
        }

        public boolean hasAxisElement() { 
          return this.axis != null && !this.axis.isEmpty();
        }

        public boolean hasAxis() { 
          return this.axis != null && !this.axis.isEmpty();
        }

        /**
         * @param value {@link #axis} (Adjustment for astigmatism measured in integer degrees.). This is the underlying object with id, value and extensions. The accessor "getAxis" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setAxisElement(IntegerType value) { 
          this.axis = value;
          return this;
        }

        /**
         * @return Adjustment for astigmatism measured in integer degrees.
         */
        public int getAxis() { 
          return this.axis == null ? 0 : this.axis.getValue();
        }

        /**
         * @param value Adjustment for astigmatism measured in integer degrees.
         */
        public VisionPrescriptionDispenseComponent setAxis(int value) { 
            if (this.axis == null)
              this.axis = new IntegerType();
            this.axis.setValue(value);
          return this;
        }

        /**
         * @return {@link #prism} (Amount of prism to compensate for eye alignment in fractional units.). This is the underlying object with id, value and extensions. The accessor "getPrism" gives direct access to the value
         */
        public DecimalType getPrismElement() { 
          if (this.prism == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.prism");
            else if (Configuration.doAutoCreate())
              this.prism = new DecimalType(); // bb
          return this.prism;
        }

        public boolean hasPrismElement() { 
          return this.prism != null && !this.prism.isEmpty();
        }

        public boolean hasPrism() { 
          return this.prism != null && !this.prism.isEmpty();
        }

        /**
         * @param value {@link #prism} (Amount of prism to compensate for eye alignment in fractional units.). This is the underlying object with id, value and extensions. The accessor "getPrism" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setPrismElement(DecimalType value) { 
          this.prism = value;
          return this;
        }

        /**
         * @return Amount of prism to compensate for eye alignment in fractional units.
         */
        public BigDecimal getPrism() { 
          return this.prism == null ? null : this.prism.getValue();
        }

        /**
         * @param value Amount of prism to compensate for eye alignment in fractional units.
         */
        public VisionPrescriptionDispenseComponent setPrism(BigDecimal value) { 
          if (value == null)
            this.prism = null;
          else {
            if (this.prism == null)
              this.prism = new DecimalType();
            this.prism.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #base} (The relative base, or reference lens edge, for the prism.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
         */
        public Enumeration<BaseCodes> getBaseElement() { 
          if (this.base == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.base");
            else if (Configuration.doAutoCreate())
              this.base = new Enumeration<BaseCodes>(new BaseCodesEnumFactory()); // bb
          return this.base;
        }

        public boolean hasBaseElement() { 
          return this.base != null && !this.base.isEmpty();
        }

        public boolean hasBase() { 
          return this.base != null && !this.base.isEmpty();
        }

        /**
         * @param value {@link #base} (The relative base, or reference lens edge, for the prism.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setBaseElement(Enumeration<BaseCodes> value) { 
          this.base = value;
          return this;
        }

        /**
         * @return The relative base, or reference lens edge, for the prism.
         */
        public BaseCodes getBase() { 
          return this.base == null ? null : this.base.getValue();
        }

        /**
         * @param value The relative base, or reference lens edge, for the prism.
         */
        public VisionPrescriptionDispenseComponent setBase(BaseCodes value) { 
          if (value == null)
            this.base = null;
          else {
            if (this.base == null)
              this.base = new Enumeration<BaseCodes>(new BaseCodesEnumFactory());
            this.base.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #add} (Power adjustment for multifocal lenses measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getAdd" gives direct access to the value
         */
        public DecimalType getAddElement() { 
          if (this.add == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.add");
            else if (Configuration.doAutoCreate())
              this.add = new DecimalType(); // bb
          return this.add;
        }

        public boolean hasAddElement() { 
          return this.add != null && !this.add.isEmpty();
        }

        public boolean hasAdd() { 
          return this.add != null && !this.add.isEmpty();
        }

        /**
         * @param value {@link #add} (Power adjustment for multifocal lenses measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getAdd" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setAddElement(DecimalType value) { 
          this.add = value;
          return this;
        }

        /**
         * @return Power adjustment for multifocal lenses measured in diopters (0.25 units).
         */
        public BigDecimal getAdd() { 
          return this.add == null ? null : this.add.getValue();
        }

        /**
         * @param value Power adjustment for multifocal lenses measured in diopters (0.25 units).
         */
        public VisionPrescriptionDispenseComponent setAdd(BigDecimal value) { 
          if (value == null)
            this.add = null;
          else {
            if (this.add == null)
              this.add = new DecimalType();
            this.add.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #power} (Contact lens power measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getPower" gives direct access to the value
         */
        public DecimalType getPowerElement() { 
          if (this.power == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.power");
            else if (Configuration.doAutoCreate())
              this.power = new DecimalType(); // bb
          return this.power;
        }

        public boolean hasPowerElement() { 
          return this.power != null && !this.power.isEmpty();
        }

        public boolean hasPower() { 
          return this.power != null && !this.power.isEmpty();
        }

        /**
         * @param value {@link #power} (Contact lens power measured in diopters (0.25 units).). This is the underlying object with id, value and extensions. The accessor "getPower" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setPowerElement(DecimalType value) { 
          this.power = value;
          return this;
        }

        /**
         * @return Contact lens power measured in diopters (0.25 units).
         */
        public BigDecimal getPower() { 
          return this.power == null ? null : this.power.getValue();
        }

        /**
         * @param value Contact lens power measured in diopters (0.25 units).
         */
        public VisionPrescriptionDispenseComponent setPower(BigDecimal value) { 
          if (value == null)
            this.power = null;
          else {
            if (this.power == null)
              this.power = new DecimalType();
            this.power.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #backCurve} (Back Curvature measured in millimeters.). This is the underlying object with id, value and extensions. The accessor "getBackCurve" gives direct access to the value
         */
        public DecimalType getBackCurveElement() { 
          if (this.backCurve == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.backCurve");
            else if (Configuration.doAutoCreate())
              this.backCurve = new DecimalType(); // bb
          return this.backCurve;
        }

        public boolean hasBackCurveElement() { 
          return this.backCurve != null && !this.backCurve.isEmpty();
        }

        public boolean hasBackCurve() { 
          return this.backCurve != null && !this.backCurve.isEmpty();
        }

        /**
         * @param value {@link #backCurve} (Back Curvature measured in millimeters.). This is the underlying object with id, value and extensions. The accessor "getBackCurve" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setBackCurveElement(DecimalType value) { 
          this.backCurve = value;
          return this;
        }

        /**
         * @return Back Curvature measured in millimeters.
         */
        public BigDecimal getBackCurve() { 
          return this.backCurve == null ? null : this.backCurve.getValue();
        }

        /**
         * @param value Back Curvature measured in millimeters.
         */
        public VisionPrescriptionDispenseComponent setBackCurve(BigDecimal value) { 
          if (value == null)
            this.backCurve = null;
          else {
            if (this.backCurve == null)
              this.backCurve = new DecimalType();
            this.backCurve.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #diameter} (Contact lens diameter measured in millimeters.). This is the underlying object with id, value and extensions. The accessor "getDiameter" gives direct access to the value
         */
        public DecimalType getDiameterElement() { 
          if (this.diameter == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.diameter");
            else if (Configuration.doAutoCreate())
              this.diameter = new DecimalType(); // bb
          return this.diameter;
        }

        public boolean hasDiameterElement() { 
          return this.diameter != null && !this.diameter.isEmpty();
        }

        public boolean hasDiameter() { 
          return this.diameter != null && !this.diameter.isEmpty();
        }

        /**
         * @param value {@link #diameter} (Contact lens diameter measured in millimeters.). This is the underlying object with id, value and extensions. The accessor "getDiameter" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setDiameterElement(DecimalType value) { 
          this.diameter = value;
          return this;
        }

        /**
         * @return Contact lens diameter measured in millimeters.
         */
        public BigDecimal getDiameter() { 
          return this.diameter == null ? null : this.diameter.getValue();
        }

        /**
         * @param value Contact lens diameter measured in millimeters.
         */
        public VisionPrescriptionDispenseComponent setDiameter(BigDecimal value) { 
          if (value == null)
            this.diameter = null;
          else {
            if (this.diameter == null)
              this.diameter = new DecimalType();
            this.diameter.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #duration} (The recommended maximum wear period for the lens.)
         */
        public Quantity getDuration() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new Quantity(); // cc
          return this.duration;
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (The recommended maximum wear period for the lens.)
         */
        public VisionPrescriptionDispenseComponent setDuration(Quantity value) { 
          this.duration = value;
          return this;
        }

        /**
         * @return {@link #color} (Special color or pattern.). This is the underlying object with id, value and extensions. The accessor "getColor" gives direct access to the value
         */
        public StringType getColorElement() { 
          if (this.color == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.color");
            else if (Configuration.doAutoCreate())
              this.color = new StringType(); // bb
          return this.color;
        }

        public boolean hasColorElement() { 
          return this.color != null && !this.color.isEmpty();
        }

        public boolean hasColor() { 
          return this.color != null && !this.color.isEmpty();
        }

        /**
         * @param value {@link #color} (Special color or pattern.). This is the underlying object with id, value and extensions. The accessor "getColor" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setColorElement(StringType value) { 
          this.color = value;
          return this;
        }

        /**
         * @return Special color or pattern.
         */
        public String getColor() { 
          return this.color == null ? null : this.color.getValue();
        }

        /**
         * @param value Special color or pattern.
         */
        public VisionPrescriptionDispenseComponent setColor(String value) { 
          if (Utilities.noString(value))
            this.color = null;
          else {
            if (this.color == null)
              this.color = new StringType();
            this.color.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #brand} (Brand recommendations or restrictions.). This is the underlying object with id, value and extensions. The accessor "getBrand" gives direct access to the value
         */
        public StringType getBrandElement() { 
          if (this.brand == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.brand");
            else if (Configuration.doAutoCreate())
              this.brand = new StringType(); // bb
          return this.brand;
        }

        public boolean hasBrandElement() { 
          return this.brand != null && !this.brand.isEmpty();
        }

        public boolean hasBrand() { 
          return this.brand != null && !this.brand.isEmpty();
        }

        /**
         * @param value {@link #brand} (Brand recommendations or restrictions.). This is the underlying object with id, value and extensions. The accessor "getBrand" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setBrandElement(StringType value) { 
          this.brand = value;
          return this;
        }

        /**
         * @return Brand recommendations or restrictions.
         */
        public String getBrand() { 
          return this.brand == null ? null : this.brand.getValue();
        }

        /**
         * @param value Brand recommendations or restrictions.
         */
        public VisionPrescriptionDispenseComponent setBrand(String value) { 
          if (Utilities.noString(value))
            this.brand = null;
          else {
            if (this.brand == null)
              this.brand = new StringType();
            this.brand.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #notes} (Notes for special requirements such as coatings and lens materials.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
         */
        public StringType getNotesElement() { 
          if (this.notes == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VisionPrescriptionDispenseComponent.notes");
            else if (Configuration.doAutoCreate())
              this.notes = new StringType(); // bb
          return this.notes;
        }

        public boolean hasNotesElement() { 
          return this.notes != null && !this.notes.isEmpty();
        }

        public boolean hasNotes() { 
          return this.notes != null && !this.notes.isEmpty();
        }

        /**
         * @param value {@link #notes} (Notes for special requirements such as coatings and lens materials.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
         */
        public VisionPrescriptionDispenseComponent setNotesElement(StringType value) { 
          this.notes = value;
          return this;
        }

        /**
         * @return Notes for special requirements such as coatings and lens materials.
         */
        public String getNotes() { 
          return this.notes == null ? null : this.notes.getValue();
        }

        /**
         * @param value Notes for special requirements such as coatings and lens materials.
         */
        public VisionPrescriptionDispenseComponent setNotes(String value) { 
          if (Utilities.noString(value))
            this.notes = null;
          else {
            if (this.notes == null)
              this.notes = new StringType();
            this.notes.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("product", "Coding", "Identifies the type of Vision correction product which is required for the patient.", 0, java.lang.Integer.MAX_VALUE, product));
          childrenList.add(new Property("eye", "code", "The eye for which the lens applies.", 0, java.lang.Integer.MAX_VALUE, eye));
          childrenList.add(new Property("sphere", "decimal", "Lens power measured in diopters (0.25 units).", 0, java.lang.Integer.MAX_VALUE, sphere));
          childrenList.add(new Property("cylinder", "decimal", "Power adjustment for astigmatism measured in diopters (0.25 units).", 0, java.lang.Integer.MAX_VALUE, cylinder));
          childrenList.add(new Property("axis", "integer", "Adjustment for astigmatism measured in integer degrees.", 0, java.lang.Integer.MAX_VALUE, axis));
          childrenList.add(new Property("prism", "decimal", "Amount of prism to compensate for eye alignment in fractional units.", 0, java.lang.Integer.MAX_VALUE, prism));
          childrenList.add(new Property("base", "code", "The relative base, or reference lens edge, for the prism.", 0, java.lang.Integer.MAX_VALUE, base));
          childrenList.add(new Property("add", "decimal", "Power adjustment for multifocal lenses measured in diopters (0.25 units).", 0, java.lang.Integer.MAX_VALUE, add));
          childrenList.add(new Property("power", "decimal", "Contact lens power measured in diopters (0.25 units).", 0, java.lang.Integer.MAX_VALUE, power));
          childrenList.add(new Property("backCurve", "decimal", "Back Curvature measured in millimeters.", 0, java.lang.Integer.MAX_VALUE, backCurve));
          childrenList.add(new Property("diameter", "decimal", "Contact lens diameter measured in millimeters.", 0, java.lang.Integer.MAX_VALUE, diameter));
          childrenList.add(new Property("duration", "Quantity", "The recommended maximum wear period for the lens.", 0, java.lang.Integer.MAX_VALUE, duration));
          childrenList.add(new Property("color", "string", "Special color or pattern.", 0, java.lang.Integer.MAX_VALUE, color));
          childrenList.add(new Property("brand", "string", "Brand recommendations or restrictions.", 0, java.lang.Integer.MAX_VALUE, brand));
          childrenList.add(new Property("notes", "string", "Notes for special requirements such as coatings and lens materials.", 0, java.lang.Integer.MAX_VALUE, notes));
        }

      public VisionPrescriptionDispenseComponent copy() {
        VisionPrescriptionDispenseComponent dst = new VisionPrescriptionDispenseComponent();
        copyValues(dst);
        dst.product = product == null ? null : product.copy();
        dst.eye = eye == null ? null : eye.copy();
        dst.sphere = sphere == null ? null : sphere.copy();
        dst.cylinder = cylinder == null ? null : cylinder.copy();
        dst.axis = axis == null ? null : axis.copy();
        dst.prism = prism == null ? null : prism.copy();
        dst.base = base == null ? null : base.copy();
        dst.add = add == null ? null : add.copy();
        dst.power = power == null ? null : power.copy();
        dst.backCurve = backCurve == null ? null : backCurve.copy();
        dst.diameter = diameter == null ? null : diameter.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.color = color == null ? null : color.copy();
        dst.brand = brand == null ? null : brand.copy();
        dst.notes = notes == null ? null : notes.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof VisionPrescriptionDispenseComponent))
          return false;
        VisionPrescriptionDispenseComponent o = (VisionPrescriptionDispenseComponent) other;
        return compareDeep(product, o.product, true) && compareDeep(eye, o.eye, true) && compareDeep(sphere, o.sphere, true)
           && compareDeep(cylinder, o.cylinder, true) && compareDeep(axis, o.axis, true) && compareDeep(prism, o.prism, true)
           && compareDeep(base, o.base, true) && compareDeep(add, o.add, true) && compareDeep(power, o.power, true)
           && compareDeep(backCurve, o.backCurve, true) && compareDeep(diameter, o.diameter, true) && compareDeep(duration, o.duration, true)
           && compareDeep(color, o.color, true) && compareDeep(brand, o.brand, true) && compareDeep(notes, o.notes, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof VisionPrescriptionDispenseComponent))
          return false;
        VisionPrescriptionDispenseComponent o = (VisionPrescriptionDispenseComponent) other;
        return compareValues(eye, o.eye, true) && compareValues(sphere, o.sphere, true) && compareValues(cylinder, o.cylinder, true)
           && compareValues(axis, o.axis, true) && compareValues(prism, o.prism, true) && compareValues(base, o.base, true)
           && compareValues(add, o.add, true) && compareValues(power, o.power, true) && compareValues(backCurve, o.backCurve, true)
           && compareValues(diameter, o.diameter, true) && compareValues(color, o.color, true) && compareValues(brand, o.brand, true)
           && compareValues(notes, o.notes, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (product == null || product.isEmpty()) && (eye == null || eye.isEmpty())
           && (sphere == null || sphere.isEmpty()) && (cylinder == null || cylinder.isEmpty()) && (axis == null || axis.isEmpty())
           && (prism == null || prism.isEmpty()) && (base == null || base.isEmpty()) && (add == null || add.isEmpty())
           && (power == null || power.isEmpty()) && (backCurve == null || backCurve.isEmpty()) && (diameter == null || diameter.isEmpty())
           && (duration == null || duration.isEmpty()) && (color == null || color.isEmpty()) && (brand == null || brand.isEmpty())
           && (notes == null || notes.isEmpty());
      }

  }

    /**
     * Business identifier which may be used by other parties to reference or identify the prescription.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Business identifier", formalDefinition="Business identifier which may be used by other parties to reference or identify the prescription." )
    protected List<Identifier> identifier;

    /**
     * The date (and perhaps time) when the prescription was written.
     */
    @Child(name = "dateWritten", type = {DateTimeType.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="When prescription was authorized", formalDefinition="The date (and perhaps time) when the prescription was written." )
    protected DateTimeType dateWritten;

    /**
     * A link to a resource representing the person to whom the Vision products will be supplied.
     */
    @Child(name = "patient", type = {Patient.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Who prescription is for", formalDefinition="A link to a resource representing the person to whom the Vision products will be supplied." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person to whom the Vision products will be supplied.)
     */
    protected Patient patientTarget;

    /**
     * The healthcare professional responsible for authorizing the prescription.
     */
    @Child(name = "prescriber", type = {Practitioner.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Who authorizes the Vision product", formalDefinition="The healthcare professional responsible for authorizing the prescription." )
    protected Reference prescriber;

    /**
     * The actual object that is the target of the reference (The healthcare professional responsible for authorizing the prescription.)
     */
    protected Practitioner prescriberTarget;

    /**
     * A link to a resource that identifies the particular occurrence of contact between patient and health care provider.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Created during encounter / admission / stay", formalDefinition="A link to a resource that identifies the particular occurrence of contact between patient and health care provider." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    protected Encounter encounterTarget;

    /**
     * Can be the reason or the indication for writing the prescription.
     */
    @Child(name = "reason", type = {CodeableConcept.class, Condition.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Reason or indication for writing the prescription", formalDefinition="Can be the reason or the indication for writing the prescription." )
    protected Type reason;

    /**
     * Deals with details of the dispense part of the supply specification.
     */
    @Child(name = "dispense", type = {}, order = 6, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Vision supply authorization", formalDefinition="Deals with details of the dispense part of the supply specification." )
    protected List<VisionPrescriptionDispenseComponent> dispense;

    private static final long serialVersionUID = -1108276057L;

    public VisionPrescription() {
      super();
    }

    /**
     * @return {@link #identifier} (Business identifier which may be used by other parties to reference or identify the prescription.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Business identifier which may be used by other parties to reference or identify the prescription.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #dateWritten} (The date (and perhaps time) when the prescription was written.). This is the underlying object with id, value and extensions. The accessor "getDateWritten" gives direct access to the value
     */
    public DateTimeType getDateWrittenElement() { 
      if (this.dateWritten == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VisionPrescription.dateWritten");
        else if (Configuration.doAutoCreate())
          this.dateWritten = new DateTimeType(); // bb
      return this.dateWritten;
    }

    public boolean hasDateWrittenElement() { 
      return this.dateWritten != null && !this.dateWritten.isEmpty();
    }

    public boolean hasDateWritten() { 
      return this.dateWritten != null && !this.dateWritten.isEmpty();
    }

    /**
     * @param value {@link #dateWritten} (The date (and perhaps time) when the prescription was written.). This is the underlying object with id, value and extensions. The accessor "getDateWritten" gives direct access to the value
     */
    public VisionPrescription setDateWrittenElement(DateTimeType value) { 
      this.dateWritten = value;
      return this;
    }

    /**
     * @return The date (and perhaps time) when the prescription was written.
     */
    public Date getDateWritten() { 
      return this.dateWritten == null ? null : this.dateWritten.getValue();
    }

    /**
     * @param value The date (and perhaps time) when the prescription was written.
     */
    public VisionPrescription setDateWritten(Date value) { 
      if (value == null)
        this.dateWritten = null;
      else {
        if (this.dateWritten == null)
          this.dateWritten = new DateTimeType();
        this.dateWritten.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (A link to a resource representing the person to whom the Vision products will be supplied.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VisionPrescription.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A link to a resource representing the person to whom the Vision products will be supplied.)
     */
    public VisionPrescription setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the Vision products will be supplied.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VisionPrescription.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the Vision products will be supplied.)
     */
    public VisionPrescription setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #prescriber} (The healthcare professional responsible for authorizing the prescription.)
     */
    public Reference getPrescriber() { 
      if (this.prescriber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VisionPrescription.prescriber");
        else if (Configuration.doAutoCreate())
          this.prescriber = new Reference(); // cc
      return this.prescriber;
    }

    public boolean hasPrescriber() { 
      return this.prescriber != null && !this.prescriber.isEmpty();
    }

    /**
     * @param value {@link #prescriber} (The healthcare professional responsible for authorizing the prescription.)
     */
    public VisionPrescription setPrescriber(Reference value) { 
      this.prescriber = value;
      return this;
    }

    /**
     * @return {@link #prescriber} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for authorizing the prescription.)
     */
    public Practitioner getPrescriberTarget() { 
      if (this.prescriberTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VisionPrescription.prescriber");
        else if (Configuration.doAutoCreate())
          this.prescriberTarget = new Practitioner(); // aa
      return this.prescriberTarget;
    }

    /**
     * @param value {@link #prescriber} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for authorizing the prescription.)
     */
    public VisionPrescription setPrescriberTarget(Practitioner value) { 
      this.prescriberTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VisionPrescription.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public VisionPrescription setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VisionPrescription.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public VisionPrescription setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public Type getReason() { 
      return this.reason;
    }

    /**
     * @return {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public CodeableConcept getReasonCodeableConcept() throws Exception { 
      if (!(this.reason instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reason;
    }

    /**
     * @return {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public Reference getReasonReference() throws Exception { 
      if (!(this.reason instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (Reference) this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public VisionPrescription setReason(Type value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #dispense} (Deals with details of the dispense part of the supply specification.)
     */
    public List<VisionPrescriptionDispenseComponent> getDispense() { 
      if (this.dispense == null)
        this.dispense = new ArrayList<VisionPrescriptionDispenseComponent>();
      return this.dispense;
    }

    public boolean hasDispense() { 
      if (this.dispense == null)
        return false;
      for (VisionPrescriptionDispenseComponent item : this.dispense)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dispense} (Deals with details of the dispense part of the supply specification.)
     */
    // syntactic sugar
    public VisionPrescriptionDispenseComponent addDispense() { //3
      VisionPrescriptionDispenseComponent t = new VisionPrescriptionDispenseComponent();
      if (this.dispense == null)
        this.dispense = new ArrayList<VisionPrescriptionDispenseComponent>();
      this.dispense.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Business identifier which may be used by other parties to reference or identify the prescription.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("dateWritten", "dateTime", "The date (and perhaps time) when the prescription was written.", 0, java.lang.Integer.MAX_VALUE, dateWritten));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person to whom the Vision products will be supplied.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("prescriber", "Reference(Practitioner)", "The healthcare professional responsible for authorizing the prescription.", 0, java.lang.Integer.MAX_VALUE, prescriber));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "A link to a resource that identifies the particular occurrence of contact between patient and health care provider.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Condition)", "Can be the reason or the indication for writing the prescription.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("dispense", "", "Deals with details of the dispense part of the supply specification.", 0, java.lang.Integer.MAX_VALUE, dispense));
      }

      public VisionPrescription copy() {
        VisionPrescription dst = new VisionPrescription();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.dateWritten = dateWritten == null ? null : dateWritten.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.prescriber = prescriber == null ? null : prescriber.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.reason = reason == null ? null : reason.copy();
        if (dispense != null) {
          dst.dispense = new ArrayList<VisionPrescriptionDispenseComponent>();
          for (VisionPrescriptionDispenseComponent i : dispense)
            dst.dispense.add(i.copy());
        };
        return dst;
      }

      protected VisionPrescription typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof VisionPrescription))
          return false;
        VisionPrescription o = (VisionPrescription) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(dateWritten, o.dateWritten, true)
           && compareDeep(patient, o.patient, true) && compareDeep(prescriber, o.prescriber, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(reason, o.reason, true) && compareDeep(dispense, o.dispense, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof VisionPrescription))
          return false;
        VisionPrescription o = (VisionPrescription) other;
        return compareValues(dateWritten, o.dateWritten, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (dateWritten == null || dateWritten.isEmpty())
           && (patient == null || patient.isEmpty()) && (prescriber == null || prescriber.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (reason == null || reason.isEmpty()) && (dispense == null || dispense.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.VisionPrescription;
   }

    @SearchParamDefinition(name = "identifier", path = "VisionPrescription.identifier", description = "Return prescriptions with this external identity", type = "token")
    public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="patient", path="VisionPrescription.patient", description="The identity of a patient to list dispenses  for", type="reference" )
  public static final String SP_PATIENT = "patient";
    @SearchParamDefinition(name = "datewritten", path = "VisionPrescription.dateWritten", description = "Return prescriptions written on this date", type = "date")
    public static final String SP_DATEWRITTEN = "datewritten";
  @SearchParamDefinition(name="encounter", path="VisionPrescription.encounter", description="Return prescriptions with this encounter identity", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";

}

