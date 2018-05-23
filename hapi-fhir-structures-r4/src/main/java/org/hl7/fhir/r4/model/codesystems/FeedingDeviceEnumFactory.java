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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class FeedingDeviceEnumFactory implements EnumFactory<FeedingDevice> {

  public FeedingDevice fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("standard-nipple".equals(codeString))
      return FeedingDevice.STANDARDNIPPLE;
    if ("preemie-nipple".equals(codeString))
      return FeedingDevice.PREEMIENIPPLE;
    if ("ortho-nipple".equals(codeString))
      return FeedingDevice.ORTHONIPPLE;
    if ("sloflo-nipple".equals(codeString))
      return FeedingDevice.SLOFLONIPPLE;
    if ("midflo-nipple".equals(codeString))
      return FeedingDevice.MIDFLONIPPLE;
    if ("bigcut-nipple".equals(codeString))
      return FeedingDevice.BIGCUTNIPPLE;
    if ("haberman-bottle".equals(codeString))
      return FeedingDevice.HABERMANBOTTLE;
    if ("sippy-valve".equals(codeString))
      return FeedingDevice.SIPPYVALVE;
    if ("sippy-no-valve".equals(codeString))
      return FeedingDevice.SIPPYNOVALVE;
    if ("provale-cup".equals(codeString))
      return FeedingDevice.PROVALECUP;
    if ("glass-lid".equals(codeString))
      return FeedingDevice.GLASSLID;
    if ("handhold-cup".equals(codeString))
      return FeedingDevice.HANDHOLDCUP;
    if ("rubber-mat".equals(codeString))
      return FeedingDevice.RUBBERMAT;
    if ("straw".equals(codeString))
      return FeedingDevice.STRAW;
    if ("nose-cup".equals(codeString))
      return FeedingDevice.NOSECUP;
    if ("scoop-plate".equals(codeString))
      return FeedingDevice.SCOOPPLATE;
    if ("utensil-holder".equals(codeString))
      return FeedingDevice.UTENSILHOLDER;
    if ("foam-handle".equals(codeString))
      return FeedingDevice.FOAMHANDLE;
    if ("angled-utensil".equals(codeString))
      return FeedingDevice.ANGLEDUTENSIL;
    if ("spout-cup".equals(codeString))
      return FeedingDevice.SPOUTCUP;
    if ("autofeeding-device".equals(codeString))
      return FeedingDevice.AUTOFEEDINGDEVICE;
    if ("rocker-knife".equals(codeString))
      return FeedingDevice.ROCKERKNIFE;
    throw new IllegalArgumentException("Unknown FeedingDevice code '"+codeString+"'");
  }

  public String toCode(FeedingDevice code) {
    if (code == FeedingDevice.STANDARDNIPPLE)
      return "standard-nipple";
    if (code == FeedingDevice.PREEMIENIPPLE)
      return "preemie-nipple";
    if (code == FeedingDevice.ORTHONIPPLE)
      return "ortho-nipple";
    if (code == FeedingDevice.SLOFLONIPPLE)
      return "sloflo-nipple";
    if (code == FeedingDevice.MIDFLONIPPLE)
      return "midflo-nipple";
    if (code == FeedingDevice.BIGCUTNIPPLE)
      return "bigcut-nipple";
    if (code == FeedingDevice.HABERMANBOTTLE)
      return "haberman-bottle";
    if (code == FeedingDevice.SIPPYVALVE)
      return "sippy-valve";
    if (code == FeedingDevice.SIPPYNOVALVE)
      return "sippy-no-valve";
    if (code == FeedingDevice.PROVALECUP)
      return "provale-cup";
    if (code == FeedingDevice.GLASSLID)
      return "glass-lid";
    if (code == FeedingDevice.HANDHOLDCUP)
      return "handhold-cup";
    if (code == FeedingDevice.RUBBERMAT)
      return "rubber-mat";
    if (code == FeedingDevice.STRAW)
      return "straw";
    if (code == FeedingDevice.NOSECUP)
      return "nose-cup";
    if (code == FeedingDevice.SCOOPPLATE)
      return "scoop-plate";
    if (code == FeedingDevice.UTENSILHOLDER)
      return "utensil-holder";
    if (code == FeedingDevice.FOAMHANDLE)
      return "foam-handle";
    if (code == FeedingDevice.ANGLEDUTENSIL)
      return "angled-utensil";
    if (code == FeedingDevice.SPOUTCUP)
      return "spout-cup";
    if (code == FeedingDevice.AUTOFEEDINGDEVICE)
      return "autofeeding-device";
    if (code == FeedingDevice.ROCKERKNIFE)
      return "rocker-knife";
    return "?";
  }

    public String toSystem(FeedingDevice code) {
      return code.getSystem();
      }

}

