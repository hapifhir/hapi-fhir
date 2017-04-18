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

public enum V3ProbabilityDistributionType {

        /**
         * The beta-distribution is used for data that is bounded on both sides and may or may not be skewed (e.g., occurs when probabilities are estimated.)  Two parameters a and b  are available to adjust the curve.  The mean m and variance s2 relate as follows: m = a/ (a + b) and s2 = ab/((a + b)2 (a + b + 1)).
         */
        B, 
        /**
         * Used for data that describes extinction.  The exponential distribution is a special form of g-distribution where a = 1, hence, the relationship to mean m and variance s2 are m = b and s2 = b2.
         */
        E, 
        /**
         * Used to describe the quotient of two c2 random variables.  The F-distribution has two parameters n1 and n2, which are the numbers of degrees of freedom of the numerator and denominator variable respectively. The relationship to mean m  and variance s2 are: m = n2 / (n2 - 2) and s2 = (2 n2 (n2 + n1 - 2)) / (n1 (n2 - 2)2 (n2 - 4)).
         */
        F, 
        /**
         * The gamma-distribution used for data that is skewed and bounded to the right, i.e. where the maximum of the distribution curve is located near the origin.  The g-distribution has a two parameters a and b.  The relationship to mean m and variance s2 is m = a b and s2 = a b2.
         */
        G, 
        /**
         * The logarithmic normal distribution is used to transform skewed random variable X into a normally distributed random variable U = log X. The log-normal distribution can be specified with the properties mean m and standard deviation s.  Note however that mean m and standard deviation s are the parameters of the raw value distribution, not the transformed parameters of the lognormal distribution that are conventionally referred to by the same letters.  Those log-normal parameters mlog and slog relate to the mean m and standard deviation s of the data value through slog2 = log (s2/m2 + 1) and mlog = log m - slog2/2.
         */
        LN, 
        /**
         * This is the well-known bell-shaped normal distribution.  Because of the central limit theorem, the normal distribution is the distribution of choice for an unbounded random variable that is an outcome of a combination of many stochastic processes.  Even for values bounded on a single side (i.e. greater than 0) the normal distribution may be accurate enough if the mean is "far away" from the bound of the scale measured in terms of standard deviations.
         */
        N, 
        /**
         * Used to describe the quotient of a normal random variable and the square root of a c2 random variable.  The t-distribution has one parameter n, the number of degrees of freedom. The relationship to mean m  and variance s2 are: m = 0 and s2 = n / (n - 2)
         */
        T, 
        /**
         * The uniform distribution assigns a constant probability over the entire interval of possible outcomes, while all outcomes outside this interval are assumed to have zero probability.  The width of this interval is 2s sqrt(3).  Thus, the uniform distribution assigns the probability densities f(x) = sqrt(2 s sqrt(3))  to values m - s sqrt(3) >= x <= m + s sqrt(3) and f(x) = 0 otherwise.
         */
        U, 
        /**
         * Used to describe the sum of squares of random variables which occurs when a variance is estimated (rather than presumed) from the sample.  The only parameter of the c2-distribution is n, so called the number of degrees of freedom (which is the number of independent parts in the sum).  The c2-distribution is a special type of g-distribution with parameter a = n /2 and b  = 2.  Hence, m = n and s2 = 2 n.
         */
        X2, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ProbabilityDistributionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("B".equals(codeString))
          return B;
        if ("E".equals(codeString))
          return E;
        if ("F".equals(codeString))
          return F;
        if ("G".equals(codeString))
          return G;
        if ("LN".equals(codeString))
          return LN;
        if ("N".equals(codeString))
          return N;
        if ("T".equals(codeString))
          return T;
        if ("U".equals(codeString))
          return U;
        if ("X2".equals(codeString))
          return X2;
        throw new FHIRException("Unknown V3ProbabilityDistributionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case B: return "B";
            case E: return "E";
            case F: return "F";
            case G: return "G";
            case LN: return "LN";
            case N: return "N";
            case T: return "T";
            case U: return "U";
            case X2: return "X2";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ProbabilityDistributionType";
        }
        public String getDefinition() {
          switch (this) {
            case B: return "The beta-distribution is used for data that is bounded on both sides and may or may not be skewed (e.g., occurs when probabilities are estimated.)  Two parameters a and b  are available to adjust the curve.  The mean m and variance s2 relate as follows: m = a/ (a + b) and s2 = ab/((a + b)2 (a + b + 1)).";
            case E: return "Used for data that describes extinction.  The exponential distribution is a special form of g-distribution where a = 1, hence, the relationship to mean m and variance s2 are m = b and s2 = b2.";
            case F: return "Used to describe the quotient of two c2 random variables.  The F-distribution has two parameters n1 and n2, which are the numbers of degrees of freedom of the numerator and denominator variable respectively. The relationship to mean m  and variance s2 are: m = n2 / (n2 - 2) and s2 = (2 n2 (n2 + n1 - 2)) / (n1 (n2 - 2)2 (n2 - 4)).";
            case G: return "The gamma-distribution used for data that is skewed and bounded to the right, i.e. where the maximum of the distribution curve is located near the origin.  The g-distribution has a two parameters a and b.  The relationship to mean m and variance s2 is m = a b and s2 = a b2.";
            case LN: return "The logarithmic normal distribution is used to transform skewed random variable X into a normally distributed random variable U = log X. The log-normal distribution can be specified with the properties mean m and standard deviation s.  Note however that mean m and standard deviation s are the parameters of the raw value distribution, not the transformed parameters of the lognormal distribution that are conventionally referred to by the same letters.  Those log-normal parameters mlog and slog relate to the mean m and standard deviation s of the data value through slog2 = log (s2/m2 + 1) and mlog = log m - slog2/2.";
            case N: return "This is the well-known bell-shaped normal distribution.  Because of the central limit theorem, the normal distribution is the distribution of choice for an unbounded random variable that is an outcome of a combination of many stochastic processes.  Even for values bounded on a single side (i.e. greater than 0) the normal distribution may be accurate enough if the mean is \"far away\" from the bound of the scale measured in terms of standard deviations.";
            case T: return "Used to describe the quotient of a normal random variable and the square root of a c2 random variable.  The t-distribution has one parameter n, the number of degrees of freedom. The relationship to mean m  and variance s2 are: m = 0 and s2 = n / (n - 2)";
            case U: return "The uniform distribution assigns a constant probability over the entire interval of possible outcomes, while all outcomes outside this interval are assumed to have zero probability.  The width of this interval is 2s sqrt(3).  Thus, the uniform distribution assigns the probability densities f(x) = sqrt(2 s sqrt(3))  to values m - s sqrt(3) >= x <= m + s sqrt(3) and f(x) = 0 otherwise.";
            case X2: return "Used to describe the sum of squares of random variables which occurs when a variance is estimated (rather than presumed) from the sample.  The only parameter of the c2-distribution is n, so called the number of degrees of freedom (which is the number of independent parts in the sum).  The c2-distribution is a special type of g-distribution with parameter a = n /2 and b  = 2.  Hence, m = n and s2 = 2 n.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case B: return "beta";
            case E: return "exponential";
            case F: return "F";
            case G: return "(gamma)";
            case LN: return "log-normal";
            case N: return "normal (Gaussian)";
            case T: return "T";
            case U: return "uniform";
            case X2: return "chi square";
            default: return "?";
          }
    }


}

