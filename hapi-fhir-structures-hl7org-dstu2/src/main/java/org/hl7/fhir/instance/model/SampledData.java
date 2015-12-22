package org.hl7.fhir.instance.model;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.List;

import org.hl7.fhir.instance.model.api.ICompositeType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
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
    @Child(name = "data", type = {StringType.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Decimal values with spaces, or \"E\" | \"U\" | \"L\"", formalDefinition="A series of data points which are decimal values separated by a single space (character u20). The special values \"E\" (error), \"L\" (below detection limit) and \"U\" (above detection limit) can also be used in place of a decimal value." )
    protected StringType data;

    private static final long serialVersionUID = -1763278368L;

  /*
   * Constructor
   */
    public SampledData() {
      super();
    }

  /*
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
        return super.isEmpty() && (origin == null || origin.isEmpty()) && (period == null || period.isEmpty())
           && (factor == null || factor.isEmpty()) && (lowerLimit == null || lowerLimit.isEmpty()) && (upperLimit == null || upperLimit.isEmpty())
           && (dimensions == null || dimensions.isEmpty()) && (data == null || data.isEmpty());
      }


}

