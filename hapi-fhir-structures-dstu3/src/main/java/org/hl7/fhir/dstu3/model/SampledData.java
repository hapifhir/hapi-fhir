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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
 */
@DatatypeDef(name="SampledData")
public class SampledData extends Type implements ICompositeType {

    /**
     * The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series.
     */
    @Child(name = "origin", type = {SimpleQuantity.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Zero value and units", formalDefinition="The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series." )
    protected SimpleQuantity origin;

    /**
     * The length of time between sampling times, measured in milliseconds.
     */
    @Child(name = "period", type = {DecimalType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of milliseconds between samples", formalDefinition="The length of time between sampling times, measured in milliseconds." )
    protected DecimalType period;

    /**
     * A correction factor that is applied to the sampled data points before they are added to the origin.
     */
    @Child(name = "factor", type = {DecimalType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Multiply data by this before adding to origin", formalDefinition="A correction factor that is applied to the sampled data points before they are added to the origin." )
    protected DecimalType factor;

    /**
     * The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit).
     */
    @Child(name = "lowerLimit", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Lower limit of detection", formalDefinition="The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit)." )
    protected DecimalType lowerLimit;

    /**
     * The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit).
     */
    @Child(name = "upperLimit", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Upper limit of detection", formalDefinition="The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit)." )
    protected DecimalType upperLimit;

    /**
     * The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once.
     */
    @Child(name = "dimensions", type = {PositiveIntType.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of sample points at each time point", formalDefinition="The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once." )
    protected PositiveIntType dimensions;

    /**
     * A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.
     */
    @Child(name = "data", type = {StringType.class}, order=6, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Decimal values with spaces, or \"E\" | \"U\" | \"L\"", formalDefinition="A series of data points which are decimal values separated by a single space (character u20). The special values \"E\" (error), \"L\" (below detection limit) and \"U\" (above detection limit) can also be used in place of a decimal value." )
    protected StringType data;

    private static final long serialVersionUID = -1763278368L;

  /**
   * Constructor
   */
    public SampledData() {
      super();
    }

  /**
   * Constructor
   */
    public SampledData(SimpleQuantity origin, DecimalType period, PositiveIntType dimensions, StringType data) {
      super();
      this.origin = origin;
      this.period = period;
      this.dimensions = dimensions;
      this.data = data;
    }

    /**
     * @return {@link #origin} (The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series.)
     */
    public SimpleQuantity getOrigin() { 
      if (this.origin == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SampledData.origin");
        else if (Configuration.doAutoCreate())
          this.origin = new SimpleQuantity(); // cc
      return this.origin;
    }

    public boolean hasOrigin() { 
      return this.origin != null && !this.origin.isEmpty();
    }

    /**
     * @param value {@link #origin} (The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series.)
     */
    public SampledData setOrigin(SimpleQuantity value) { 
      this.origin = value;
      return this;
    }

    /**
     * @return {@link #period} (The length of time between sampling times, measured in milliseconds.). This is the underlying object with id, value and extensions. The accessor "getPeriod" gives direct access to the value
     */
    public DecimalType getPeriodElement() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SampledData.period");
        else if (Configuration.doAutoCreate())
          this.period = new DecimalType(); // bb
      return this.period;
    }

    public boolean hasPeriodElement() { 
      return this.period != null && !this.period.isEmpty();
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The length of time between sampling times, measured in milliseconds.). This is the underlying object with id, value and extensions. The accessor "getPeriod" gives direct access to the value
     */
    public SampledData setPeriodElement(DecimalType value) { 
      this.period = value;
      return this;
    }

    /**
     * @return The length of time between sampling times, measured in milliseconds.
     */
    public BigDecimal getPeriod() { 
      return this.period == null ? null : this.period.getValue();
    }

    /**
     * @param value The length of time between sampling times, measured in milliseconds.
     */
    public SampledData setPeriod(BigDecimal value) { 
        if (this.period == null)
          this.period = new DecimalType();
        this.period.setValue(value);
      return this;
    }

    /**
     * @param value The length of time between sampling times, measured in milliseconds.
     */
    public SampledData setPeriod(long value) { 
          this.period = new DecimalType();
        this.period.setValue(value);
      return this;
    }

    /**
     * @param value The length of time between sampling times, measured in milliseconds.
     */
    public SampledData setPeriod(double value) { 
          this.period = new DecimalType();
        this.period.setValue(value);
      return this;
    }

    /**
     * @return {@link #factor} (A correction factor that is applied to the sampled data points before they are added to the origin.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
     */
    public DecimalType getFactorElement() { 
      if (this.factor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SampledData.factor");
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
     * @param value {@link #factor} (A correction factor that is applied to the sampled data points before they are added to the origin.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
     */
    public SampledData setFactorElement(DecimalType value) { 
      this.factor = value;
      return this;
    }

    /**
     * @return A correction factor that is applied to the sampled data points before they are added to the origin.
     */
    public BigDecimal getFactor() { 
      return this.factor == null ? null : this.factor.getValue();
    }

    /**
     * @param value A correction factor that is applied to the sampled data points before they are added to the origin.
     */
    public SampledData setFactor(BigDecimal value) { 
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
     * @param value A correction factor that is applied to the sampled data points before they are added to the origin.
     */
    public SampledData setFactor(long value) { 
          this.factor = new DecimalType();
        this.factor.setValue(value);
      return this;
    }

    /**
     * @param value A correction factor that is applied to the sampled data points before they are added to the origin.
     */
    public SampledData setFactor(double value) { 
          this.factor = new DecimalType();
        this.factor.setValue(value);
      return this;
    }

    /**
     * @return {@link #lowerLimit} (The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit).). This is the underlying object with id, value and extensions. The accessor "getLowerLimit" gives direct access to the value
     */
    public DecimalType getLowerLimitElement() { 
      if (this.lowerLimit == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SampledData.lowerLimit");
        else if (Configuration.doAutoCreate())
          this.lowerLimit = new DecimalType(); // bb
      return this.lowerLimit;
    }

    public boolean hasLowerLimitElement() { 
      return this.lowerLimit != null && !this.lowerLimit.isEmpty();
    }

    public boolean hasLowerLimit() { 
      return this.lowerLimit != null && !this.lowerLimit.isEmpty();
    }

    /**
     * @param value {@link #lowerLimit} (The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit).). This is the underlying object with id, value and extensions. The accessor "getLowerLimit" gives direct access to the value
     */
    public SampledData setLowerLimitElement(DecimalType value) { 
      this.lowerLimit = value;
      return this;
    }

    /**
     * @return The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit).
     */
    public BigDecimal getLowerLimit() { 
      return this.lowerLimit == null ? null : this.lowerLimit.getValue();
    }

    /**
     * @param value The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit).
     */
    public SampledData setLowerLimit(BigDecimal value) { 
      if (value == null)
        this.lowerLimit = null;
      else {
        if (this.lowerLimit == null)
          this.lowerLimit = new DecimalType();
        this.lowerLimit.setValue(value);
      }
      return this;
    }

    /**
     * @param value The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit).
     */
    public SampledData setLowerLimit(long value) { 
          this.lowerLimit = new DecimalType();
        this.lowerLimit.setValue(value);
      return this;
    }

    /**
     * @param value The lower limit of detection of the measured points. This is needed if any of the data points have the value "L" (lower than detection limit).
     */
    public SampledData setLowerLimit(double value) { 
          this.lowerLimit = new DecimalType();
        this.lowerLimit.setValue(value);
      return this;
    }

    /**
     * @return {@link #upperLimit} (The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit).). This is the underlying object with id, value and extensions. The accessor "getUpperLimit" gives direct access to the value
     */
    public DecimalType getUpperLimitElement() { 
      if (this.upperLimit == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SampledData.upperLimit");
        else if (Configuration.doAutoCreate())
          this.upperLimit = new DecimalType(); // bb
      return this.upperLimit;
    }

    public boolean hasUpperLimitElement() { 
      return this.upperLimit != null && !this.upperLimit.isEmpty();
    }

    public boolean hasUpperLimit() { 
      return this.upperLimit != null && !this.upperLimit.isEmpty();
    }

    /**
     * @param value {@link #upperLimit} (The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit).). This is the underlying object with id, value and extensions. The accessor "getUpperLimit" gives direct access to the value
     */
    public SampledData setUpperLimitElement(DecimalType value) { 
      this.upperLimit = value;
      return this;
    }

    /**
     * @return The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit).
     */
    public BigDecimal getUpperLimit() { 
      return this.upperLimit == null ? null : this.upperLimit.getValue();
    }

    /**
     * @param value The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit).
     */
    public SampledData setUpperLimit(BigDecimal value) { 
      if (value == null)
        this.upperLimit = null;
      else {
        if (this.upperLimit == null)
          this.upperLimit = new DecimalType();
        this.upperLimit.setValue(value);
      }
      return this;
    }

    /**
     * @param value The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit).
     */
    public SampledData setUpperLimit(long value) { 
          this.upperLimit = new DecimalType();
        this.upperLimit.setValue(value);
      return this;
    }

    /**
     * @param value The upper limit of detection of the measured points. This is needed if any of the data points have the value "U" (higher than detection limit).
     */
    public SampledData setUpperLimit(double value) { 
          this.upperLimit = new DecimalType();
        this.upperLimit.setValue(value);
      return this;
    }

    /**
     * @return {@link #dimensions} (The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once.). This is the underlying object with id, value and extensions. The accessor "getDimensions" gives direct access to the value
     */
    public PositiveIntType getDimensionsElement() { 
      if (this.dimensions == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SampledData.dimensions");
        else if (Configuration.doAutoCreate())
          this.dimensions = new PositiveIntType(); // bb
      return this.dimensions;
    }

    public boolean hasDimensionsElement() { 
      return this.dimensions != null && !this.dimensions.isEmpty();
    }

    public boolean hasDimensions() { 
      return this.dimensions != null && !this.dimensions.isEmpty();
    }

    /**
     * @param value {@link #dimensions} (The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once.). This is the underlying object with id, value and extensions. The accessor "getDimensions" gives direct access to the value
     */
    public SampledData setDimensionsElement(PositiveIntType value) { 
      this.dimensions = value;
      return this;
    }

    /**
     * @return The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once.
     */
    public int getDimensions() { 
      return this.dimensions == null || this.dimensions.isEmpty() ? 0 : this.dimensions.getValue();
    }

    /**
     * @param value The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once.
     */
    public SampledData setDimensions(int value) { 
        if (this.dimensions == null)
          this.dimensions = new PositiveIntType();
        this.dimensions.setValue(value);
      return this;
    }

    /**
     * @return {@link #data} (A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.). This is the underlying object with id, value and extensions. The accessor "getData" gives direct access to the value
     */
    public StringType getDataElement() { 
      if (this.data == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SampledData.data");
        else if (Configuration.doAutoCreate())
          this.data = new StringType(); // bb
      return this.data;
    }

    public boolean hasDataElement() { 
      return this.data != null && !this.data.isEmpty();
    }

    public boolean hasData() { 
      return this.data != null && !this.data.isEmpty();
    }

    /**
     * @param value {@link #data} (A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.). This is the underlying object with id, value and extensions. The accessor "getData" gives direct access to the value
     */
    public SampledData setDataElement(StringType value) { 
      this.data = value;
      return this;
    }

    /**
     * @return A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.
     */
    public String getData() { 
      return this.data == null ? null : this.data.getValue();
    }

    /**
     * @param value A series of data points which are decimal values separated by a single space (character u20). The special values "E" (error), "L" (below detection limit) and "U" (above detection limit) can also be used in place of a decimal value.
     */
    public SampledData setData(String value) { 
        if (this.data == null)
          this.data = new StringType();
        this.data.setValue(value);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("origin", "SimpleQuantity", "The base quantity that a measured value of zero represents. In addition, this provides the units of the entire measurement series.", 0, java.lang.Integer.MAX_VALUE, origin));
        childrenList.add(new Property("period", "decimal", "The length of time between sampling times, measured in milliseconds.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("factor", "decimal", "A correction factor that is applied to the sampled data points before they are added to the origin.", 0, java.lang.Integer.MAX_VALUE, factor));
        childrenList.add(new Property("lowerLimit", "decimal", "The lower limit of detection of the measured points. This is needed if any of the data points have the value \"L\" (lower than detection limit).", 0, java.lang.Integer.MAX_VALUE, lowerLimit));
        childrenList.add(new Property("upperLimit", "decimal", "The upper limit of detection of the measured points. This is needed if any of the data points have the value \"U\" (higher than detection limit).", 0, java.lang.Integer.MAX_VALUE, upperLimit));
        childrenList.add(new Property("dimensions", "positiveInt", "The number of sample points at each time point. If this value is greater than one, then the dimensions will be interlaced - all the sample points for a point in time will be recorded at once.", 0, java.lang.Integer.MAX_VALUE, dimensions));
        childrenList.add(new Property("data", "string", "A series of data points which are decimal values separated by a single space (character u20). The special values \"E\" (error), \"L\" (below detection limit) and \"U\" (above detection limit) can also be used in place of a decimal value.", 0, java.lang.Integer.MAX_VALUE, data));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1008619738: /*origin*/ return this.origin == null ? new Base[0] : new Base[] {this.origin}; // SimpleQuantity
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // DecimalType
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case 1209133370: /*lowerLimit*/ return this.lowerLimit == null ? new Base[0] : new Base[] {this.lowerLimit}; // DecimalType
        case -1681713095: /*upperLimit*/ return this.upperLimit == null ? new Base[0] : new Base[] {this.upperLimit}; // DecimalType
        case 414334925: /*dimensions*/ return this.dimensions == null ? new Base[0] : new Base[] {this.dimensions}; // PositiveIntType
        case 3076010: /*data*/ return this.data == null ? new Base[0] : new Base[] {this.data}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1008619738: // origin
          this.origin = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -991726143: // period
          this.period = castToDecimal(value); // DecimalType
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case 1209133370: // lowerLimit
          this.lowerLimit = castToDecimal(value); // DecimalType
          return value;
        case -1681713095: // upperLimit
          this.upperLimit = castToDecimal(value); // DecimalType
          return value;
        case 414334925: // dimensions
          this.dimensions = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3076010: // data
          this.data = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("origin")) {
          this.origin = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("period")) {
          this.period = castToDecimal(value); // DecimalType
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("lowerLimit")) {
          this.lowerLimit = castToDecimal(value); // DecimalType
        } else if (name.equals("upperLimit")) {
          this.upperLimit = castToDecimal(value); // DecimalType
        } else if (name.equals("dimensions")) {
          this.dimensions = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("data")) {
          this.data = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1008619738:  return getOrigin(); 
        case -991726143:  return getPeriodElement();
        case -1282148017:  return getFactorElement();
        case 1209133370:  return getLowerLimitElement();
        case -1681713095:  return getUpperLimitElement();
        case 414334925:  return getDimensionsElement();
        case 3076010:  return getDataElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1008619738: /*origin*/ return new String[] {"SimpleQuantity"};
        case -991726143: /*period*/ return new String[] {"decimal"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case 1209133370: /*lowerLimit*/ return new String[] {"decimal"};
        case -1681713095: /*upperLimit*/ return new String[] {"decimal"};
        case 414334925: /*dimensions*/ return new String[] {"positiveInt"};
        case 3076010: /*data*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("origin")) {
          this.origin = new SimpleQuantity();
          return this.origin;
        }
        else if (name.equals("period")) {
          throw new FHIRException("Cannot call addChild on a primitive type SampledData.period");
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type SampledData.factor");
        }
        else if (name.equals("lowerLimit")) {
          throw new FHIRException("Cannot call addChild on a primitive type SampledData.lowerLimit");
        }
        else if (name.equals("upperLimit")) {
          throw new FHIRException("Cannot call addChild on a primitive type SampledData.upperLimit");
        }
        else if (name.equals("dimensions")) {
          throw new FHIRException("Cannot call addChild on a primitive type SampledData.dimensions");
        }
        else if (name.equals("data")) {
          throw new FHIRException("Cannot call addChild on a primitive type SampledData.data");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SampledData";

  }

      public SampledData copy() {
        SampledData dst = new SampledData();
        copyValues(dst);
        dst.origin = origin == null ? null : origin.copy();
        dst.period = period == null ? null : period.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.lowerLimit = lowerLimit == null ? null : lowerLimit.copy();
        dst.upperLimit = upperLimit == null ? null : upperLimit.copy();
        dst.dimensions = dimensions == null ? null : dimensions.copy();
        dst.data = data == null ? null : data.copy();
        return dst;
      }

      protected SampledData typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SampledData))
          return false;
        SampledData o = (SampledData) other;
        return compareDeep(origin, o.origin, true) && compareDeep(period, o.period, true) && compareDeep(factor, o.factor, true)
           && compareDeep(lowerLimit, o.lowerLimit, true) && compareDeep(upperLimit, o.upperLimit, true) && compareDeep(dimensions, o.dimensions, true)
           && compareDeep(data, o.data, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SampledData))
          return false;
        SampledData o = (SampledData) other;
        return compareValues(period, o.period, true) && compareValues(factor, o.factor, true) && compareValues(lowerLimit, o.lowerLimit, true)
           && compareValues(upperLimit, o.upperLimit, true) && compareValues(dimensions, o.dimensions, true) && compareValues(data, o.data, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(origin, period, factor, lowerLimit
          , upperLimit, dimensions, data);
      }


}

