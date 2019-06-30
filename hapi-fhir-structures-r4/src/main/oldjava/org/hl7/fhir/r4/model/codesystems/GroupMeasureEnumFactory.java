package org.hl7.fhir.r4.model.codesystems;

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


import org.hl7.fhir.r4.model.EnumFactory;

public class GroupMeasureEnumFactory implements EnumFactory<GroupMeasure> {

  public GroupMeasure fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("mean".equals(codeString))
      return GroupMeasure.MEAN;
    if ("median".equals(codeString))
      return GroupMeasure.MEDIAN;
    if ("mean-of-mean".equals(codeString))
      return GroupMeasure.MEANOFMEAN;
    if ("mean-of-median".equals(codeString))
      return GroupMeasure.MEANOFMEDIAN;
    if ("median-of-mean".equals(codeString))
      return GroupMeasure.MEDIANOFMEAN;
    if ("median-of-median".equals(codeString))
      return GroupMeasure.MEDIANOFMEDIAN;
    throw new IllegalArgumentException("Unknown GroupMeasure code '"+codeString+"'");
  }

  public String toCode(GroupMeasure code) {
    if (code == GroupMeasure.MEAN)
      return "mean";
    if (code == GroupMeasure.MEDIAN)
      return "median";
    if (code == GroupMeasure.MEANOFMEAN)
      return "mean-of-mean";
    if (code == GroupMeasure.MEANOFMEDIAN)
      return "mean-of-median";
    if (code == GroupMeasure.MEDIANOFMEAN)
      return "median-of-mean";
    if (code == GroupMeasure.MEDIANOFMEDIAN)
      return "median-of-median";
    return "?";
  }

    public String toSystem(GroupMeasure code) {
      return code.getSystem();
      }

}

