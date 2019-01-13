package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


import org.hl7.fhir.dstu2016may.model.EnumFactory;

public class HspcAdmissionSourceEnumFactory implements EnumFactory<HspcAdmissionSource> {

  public HspcAdmissionSource fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("102702".equals(codeString))
      return HspcAdmissionSource._102702;
    if ("17567".equals(codeString))
      return HspcAdmissionSource._17567;
    if ("17566".equals(codeString))
      return HspcAdmissionSource._17566;
    if ("528129525".equals(codeString))
      return HspcAdmissionSource._528129525;
    if ("102703".equals(codeString))
      return HspcAdmissionSource._102703;
    if ("219107".equals(codeString))
      return HspcAdmissionSource._219107;
    if ("528129526".equals(codeString))
      return HspcAdmissionSource._528129526;
    if ("154642".equals(codeString))
      return HspcAdmissionSource._154642;
    if ("102701".equals(codeString))
      return HspcAdmissionSource._102701;
    if ("528129527".equals(codeString))
      return HspcAdmissionSource._528129527;
    if ("510105657".equals(codeString))
      return HspcAdmissionSource._510105657;
    if ("528129528".equals(codeString))
      return HspcAdmissionSource._528129528;
    if ("510105655".equals(codeString))
      return HspcAdmissionSource._510105655;
    if ("102704".equals(codeString))
      return HspcAdmissionSource._102704;
    if ("14689616".equals(codeString))
      return HspcAdmissionSource._14689616;
    if ("528129529".equals(codeString))
      return HspcAdmissionSource._528129529;
    if ("520442099".equals(codeString))
      return HspcAdmissionSource._520442099;
    if ("14690444".equals(codeString))
      return HspcAdmissionSource._14690444;
    if ("510105656".equals(codeString))
      return HspcAdmissionSource._510105656;
    if ("528129530".equals(codeString))
      return HspcAdmissionSource._528129530;
    if ("510105654".equals(codeString))
      return HspcAdmissionSource._510105654;
    throw new IllegalArgumentException("Unknown HspcAdmissionSource code '"+codeString+"'");
  }

  public String toCode(HspcAdmissionSource code) {
    if (code == HspcAdmissionSource._102702)
      return "102702";
    if (code == HspcAdmissionSource._17567)
      return "17567";
    if (code == HspcAdmissionSource._17566)
      return "17566";
    if (code == HspcAdmissionSource._528129525)
      return "528129525";
    if (code == HspcAdmissionSource._102703)
      return "102703";
    if (code == HspcAdmissionSource._219107)
      return "219107";
    if (code == HspcAdmissionSource._528129526)
      return "528129526";
    if (code == HspcAdmissionSource._154642)
      return "154642";
    if (code == HspcAdmissionSource._102701)
      return "102701";
    if (code == HspcAdmissionSource._528129527)
      return "528129527";
    if (code == HspcAdmissionSource._510105657)
      return "510105657";
    if (code == HspcAdmissionSource._528129528)
      return "528129528";
    if (code == HspcAdmissionSource._510105655)
      return "510105655";
    if (code == HspcAdmissionSource._102704)
      return "102704";
    if (code == HspcAdmissionSource._14689616)
      return "14689616";
    if (code == HspcAdmissionSource._528129529)
      return "528129529";
    if (code == HspcAdmissionSource._520442099)
      return "520442099";
    if (code == HspcAdmissionSource._14690444)
      return "14690444";
    if (code == HspcAdmissionSource._510105656)
      return "510105656";
    if (code == HspcAdmissionSource._528129530)
      return "528129530";
    if (code == HspcAdmissionSource._510105654)
      return "510105654";
    return "?";
  }

    public String toSystem(HspcAdmissionSource code) {
      return code.getSystem();
      }

}

