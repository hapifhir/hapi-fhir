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

// Generated on Tue, Dec 6, 2016 09:42-0500 for FHIR v1.8.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ObservationStatisticsEnumFactory implements EnumFactory<ObservationStatistics> {

  public ObservationStatistics fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("average".equals(codeString))
      return ObservationStatistics.AVERAGE;
    if ("maximum".equals(codeString))
      return ObservationStatistics.MAXIMUM;
    if ("minimum".equals(codeString))
      return ObservationStatistics.MINIMUM;
    if ("count".equals(codeString))
      return ObservationStatistics.COUNT;
    if ("totalcount".equals(codeString))
      return ObservationStatistics.TOTALCOUNT;
    if ("median".equals(codeString))
      return ObservationStatistics.MEDIAN;
    if ("std-dev".equals(codeString))
      return ObservationStatistics.STDDEV;
    if ("sum".equals(codeString))
      return ObservationStatistics.SUM;
    if ("variance".equals(codeString))
      return ObservationStatistics.VARIANCE;
    if ("%20".equals(codeString))
      return ObservationStatistics.PCT20;
    if ("%80".equals(codeString))
      return ObservationStatistics.PCT80;
    if ("4-lower".equals(codeString))
      return ObservationStatistics._4LOWER;
    if ("4-upper".equals(codeString))
      return ObservationStatistics._4UPPER;
    if ("4-dev".equals(codeString))
      return ObservationStatistics._4DEV;
    if ("5-1".equals(codeString))
      return ObservationStatistics._51;
    if ("5-2".equals(codeString))
      return ObservationStatistics._52;
    if ("5-3".equals(codeString))
      return ObservationStatistics._53;
    if ("5-4".equals(codeString))
      return ObservationStatistics._54;
    throw new IllegalArgumentException("Unknown ObservationStatistics code '"+codeString+"'");
  }

  public String toCode(ObservationStatistics code) {
    if (code == ObservationStatistics.AVERAGE)
      return "average";
    if (code == ObservationStatistics.MAXIMUM)
      return "maximum";
    if (code == ObservationStatistics.MINIMUM)
      return "minimum";
    if (code == ObservationStatistics.COUNT)
      return "count";
    if (code == ObservationStatistics.TOTALCOUNT)
      return "totalcount";
    if (code == ObservationStatistics.MEDIAN)
      return "median";
    if (code == ObservationStatistics.STDDEV)
      return "std-dev";
    if (code == ObservationStatistics.SUM)
      return "sum";
    if (code == ObservationStatistics.VARIANCE)
      return "variance";
    if (code == ObservationStatistics.PCT20)
      return "%20";
    if (code == ObservationStatistics.PCT80)
      return "%80";
    if (code == ObservationStatistics._4LOWER)
      return "4-lower";
    if (code == ObservationStatistics._4UPPER)
      return "4-upper";
    if (code == ObservationStatistics._4DEV)
      return "4-dev";
    if (code == ObservationStatistics._51)
      return "5-1";
    if (code == ObservationStatistics._52)
      return "5-2";
    if (code == ObservationStatistics._53)
      return "5-3";
    if (code == ObservationStatistics._54)
      return "5-4";
    return "?";
  }

    public String toSystem(ObservationStatistics code) {
      return code.getSystem();
      }

}

