package org.hl7.fhir.dstu3.model.codesystems;

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


import org.hl7.fhir.exceptions.FHIRException;

public enum ObservationStatistics {

        /**
         * The [mean](https://en.wikipedia.org/wiki/Arithmetic_mean) of N measurements over the stated period
         */
        AVERAGE, 
        /**
         * The [maximum](https://en.wikipedia.org/wiki/Maximal_element) value of N measurements over the stated period
         */
        MAXIMUM, 
        /**
         * The [minimum](https://en.wikipedia.org/wiki/Minimal_element) value of N measurements over the stated period
         */
        MINIMUM, 
        /**
         * The [number] of valid measurements over the stated period that contributed to the other statistical outputs
         */
        COUNT, 
        /**
         * The total [number] of valid measurements over the stated period, including observations that were ignored because they did not contain valid result values
         */
        TOTALCOUNT, 
        /**
         * The [median](https://en.wikipedia.org/wiki/Median) of N measurements over the stated period
         */
        MEDIAN, 
        /**
         * The [standard deviation](https://en.wikipedia.org/wiki/Standard_deviation) of N measurements over the stated period
         */
        STDDEV, 
        /**
         * The [sum](https://en.wikipedia.org/wiki/Summation) of N measurements over the stated period
         */
        SUM, 
        /**
         * The [variance](https://en.wikipedia.org/wiki/Variance) of N measurements over the stated period
         */
        VARIANCE, 
        /**
         * The 20th [Percentile](https://en.wikipedia.org/wiki/Percentile) of N measurements over the stated period
         */
        _20PERCENT, 
        /**
         * The 80th [Percentile](https://en.wikipedia.org/wiki/Percentile) of N measurements over the stated period
         */
        _80PERCENT, 
        /**
         * The lower [Quartile](https://en.wikipedia.org/wiki/Quartile) Boundary of N measurements over the stated period
         */
        _4LOWER, 
        /**
         * The upper [Quartile](https://en.wikipedia.org/wiki/Quartile) Boundary of N measurements over the stated period
         */
        _4UPPER, 
        /**
         * The difference between the upper and lower [Quartiles](https://en.wikipedia.org/wiki/Quartile) is called the Interquartile range. (IQR = Q3-Q1) Quartile deviation or Semi-interquartile range is one-half the difference between the first and the third quartiles.
         */
        _4DEV, 
        /**
         * The lowest of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population
         */
        _51, 
        /**
         * The second of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population
         */
        _52, 
        /**
         * The third of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population
         */
        _53, 
        /**
         * The fourth of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population
         */
        _54, 
        /**
         * Skewness is a measure of the asymmetry of the probability distribution of a real-valued random variable about its mean. The skewness value can be positive or negative, or even undefined.  Source: [Wikipedia](https://en.wikipedia.org/wiki/Skewness)
         */
        SKEW, 
        /**
         * Kurtosis  is a measure of the "tailedness" of the probability distribution of a real-valued random variable.   Source: [Wikipedia](https://en.wikipedia.org/wiki/Kurtosis)
         */
        KURTOSIS, 
        /**
         * Linear regression is an approach for modeling two-dimensional sample points with one independent variable and one dependent variable (conventionally, the x and y coordinates in a Cartesian coordinate system) and finds a linear function (a non-vertical straight line) that, as accurately as possible, predicts the dependent variable values as a function of the independent variables. Source: [Wikipedia](https://en.wikipedia.org/wiki/Simple_linear_regression)  This Statistic code will return both a gradient and an intercept value.
         */
        REGRESSION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObservationStatistics fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("average".equals(codeString))
          return AVERAGE;
        if ("maximum".equals(codeString))
          return MAXIMUM;
        if ("minimum".equals(codeString))
          return MINIMUM;
        if ("count".equals(codeString))
          return COUNT;
        if ("totalcount".equals(codeString))
          return TOTALCOUNT;
        if ("median".equals(codeString))
          return MEDIAN;
        if ("std-dev".equals(codeString))
          return STDDEV;
        if ("sum".equals(codeString))
          return SUM;
        if ("variance".equals(codeString))
          return VARIANCE;
        if ("20-percent".equals(codeString))
          return _20PERCENT;
        if ("80-percent".equals(codeString))
          return _80PERCENT;
        if ("4-lower".equals(codeString))
          return _4LOWER;
        if ("4-upper".equals(codeString))
          return _4UPPER;
        if ("4-dev".equals(codeString))
          return _4DEV;
        if ("5-1".equals(codeString))
          return _51;
        if ("5-2".equals(codeString))
          return _52;
        if ("5-3".equals(codeString))
          return _53;
        if ("5-4".equals(codeString))
          return _54;
        if ("skew".equals(codeString))
          return SKEW;
        if ("kurtosis".equals(codeString))
          return KURTOSIS;
        if ("regression".equals(codeString))
          return REGRESSION;
        throw new FHIRException("Unknown ObservationStatistics code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AVERAGE: return "average";
            case MAXIMUM: return "maximum";
            case MINIMUM: return "minimum";
            case COUNT: return "count";
            case TOTALCOUNT: return "totalcount";
            case MEDIAN: return "median";
            case STDDEV: return "std-dev";
            case SUM: return "sum";
            case VARIANCE: return "variance";
            case _20PERCENT: return "20-percent";
            case _80PERCENT: return "80-percent";
            case _4LOWER: return "4-lower";
            case _4UPPER: return "4-upper";
            case _4DEV: return "4-dev";
            case _51: return "5-1";
            case _52: return "5-2";
            case _53: return "5-3";
            case _54: return "5-4";
            case SKEW: return "skew";
            case KURTOSIS: return "kurtosis";
            case REGRESSION: return "regression";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/observation-statistics";
        }
        public String getDefinition() {
          switch (this) {
            case AVERAGE: return "The [mean](https://en.wikipedia.org/wiki/Arithmetic_mean) of N measurements over the stated period";
            case MAXIMUM: return "The [maximum](https://en.wikipedia.org/wiki/Maximal_element) value of N measurements over the stated period";
            case MINIMUM: return "The [minimum](https://en.wikipedia.org/wiki/Minimal_element) value of N measurements over the stated period";
            case COUNT: return "The [number] of valid measurements over the stated period that contributed to the other statistical outputs";
            case TOTALCOUNT: return "The total [number] of valid measurements over the stated period, including observations that were ignored because they did not contain valid result values";
            case MEDIAN: return "The [median](https://en.wikipedia.org/wiki/Median) of N measurements over the stated period";
            case STDDEV: return "The [standard deviation](https://en.wikipedia.org/wiki/Standard_deviation) of N measurements over the stated period";
            case SUM: return "The [sum](https://en.wikipedia.org/wiki/Summation) of N measurements over the stated period";
            case VARIANCE: return "The [variance](https://en.wikipedia.org/wiki/Variance) of N measurements over the stated period";
            case _20PERCENT: return "The 20th [Percentile](https://en.wikipedia.org/wiki/Percentile) of N measurements over the stated period";
            case _80PERCENT: return "The 80th [Percentile](https://en.wikipedia.org/wiki/Percentile) of N measurements over the stated period";
            case _4LOWER: return "The lower [Quartile](https://en.wikipedia.org/wiki/Quartile) Boundary of N measurements over the stated period";
            case _4UPPER: return "The upper [Quartile](https://en.wikipedia.org/wiki/Quartile) Boundary of N measurements over the stated period";
            case _4DEV: return "The difference between the upper and lower [Quartiles](https://en.wikipedia.org/wiki/Quartile) is called the Interquartile range. (IQR = Q3-Q1) Quartile deviation or Semi-interquartile range is one-half the difference between the first and the third quartiles.";
            case _51: return "The lowest of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population";
            case _52: return "The second of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population";
            case _53: return "The third of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population";
            case _54: return "The fourth of four values that divide the N measurements into a frequency distribution of five classes with each containing one fifth of the total population";
            case SKEW: return "Skewness is a measure of the asymmetry of the probability distribution of a real-valued random variable about its mean. The skewness value can be positive or negative, or even undefined.  Source: [Wikipedia](https://en.wikipedia.org/wiki/Skewness)";
            case KURTOSIS: return "Kurtosis  is a measure of the \"tailedness\" of the probability distribution of a real-valued random variable.   Source: [Wikipedia](https://en.wikipedia.org/wiki/Kurtosis)";
            case REGRESSION: return "Linear regression is an approach for modeling two-dimensional sample points with one independent variable and one dependent variable (conventionally, the x and y coordinates in a Cartesian coordinate system) and finds a linear function (a non-vertical straight line) that, as accurately as possible, predicts the dependent variable values as a function of the independent variables. Source: [Wikipedia](https://en.wikipedia.org/wiki/Simple_linear_regression)  This Statistic code will return both a gradient and an intercept value.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AVERAGE: return "Average";
            case MAXIMUM: return "Maximum";
            case MINIMUM: return "Minimum";
            case COUNT: return "Count";
            case TOTALCOUNT: return "Total Count";
            case MEDIAN: return "Median";
            case STDDEV: return "Standard Deviation";
            case SUM: return "Sum";
            case VARIANCE: return "Variance";
            case _20PERCENT: return "20th Percentile";
            case _80PERCENT: return "80th Percentile";
            case _4LOWER: return "Lower Quartile";
            case _4UPPER: return "Upper Quartile";
            case _4DEV: return "Quartile Deviation";
            case _51: return "1st Quintile";
            case _52: return "2nd Quintile";
            case _53: return "3rd Quintile";
            case _54: return "4th Quintile";
            case SKEW: return "Skew";
            case KURTOSIS: return "Kurtosis";
            case REGRESSION: return "Regression";
            default: return "?";
          }
    }


}

