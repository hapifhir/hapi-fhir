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

public class HspcPatientVeteranMilitaryStatusEnumFactory implements EnumFactory<HspcPatientVeteranMilitaryStatus> {

  public HspcPatientVeteranMilitaryStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("8ca8cc2b-baa0-4cb6-8863-418173c125d1".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._8CA8CC2BBAA04CB68863418173C125D1;
    if ("4eb9a0a1-3334-48df-084d-cef37c9ac0d3".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._4EB9A0A1333448DF084DCEF37C9AC0D3;
    if ("70a68183-01a4-45b1-a5e6-49a13e221713".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._70A6818301A445B1A5E649A13E221713;
    if ("189e9af6-bcbf-47e2-acee-4e5c50f54ba6".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._189E9AF6BCBF47E2ACEE4E5C50F54BA6;
    if ("bab7b911-e3cf-426f-95d6-0637d7091e07".equals(codeString))
      return HspcPatientVeteranMilitaryStatus.BAB7B911E3CF426F95D60637D7091E07;
    if ("2b98cec8-b29d-4545-9350-a2b54dbd2fe7".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._2B98CEC8B29D45459350A2B54DBD2FE7;
    if ("617936ec-d6a7-4642-a1d8-b27bf8735f9d".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._617936ECD6A74642A1D8B27BF8735F9D;
    if ("40d86a62-67a9-49c6-8f43-c1a9ef51689b".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._40D86A6267A949C68F43C1A9EF51689B;
    if ("ca02026a-3ebf-4fb4-09e0-02d89c931643".equals(codeString))
      return HspcPatientVeteranMilitaryStatus.CA02026A3EBF4FB409E002D89C931643;
    if ("75f4440a-ec37-47fe-b26d-39c09c204676".equals(codeString))
      return HspcPatientVeteranMilitaryStatus._75F4440AEC3747FEB26D39C09C204676;
    throw new IllegalArgumentException("Unknown HspcPatientVeteranMilitaryStatus code '"+codeString+"'");
  }

  public String toCode(HspcPatientVeteranMilitaryStatus code) {
    if (code == HspcPatientVeteranMilitaryStatus._8CA8CC2BBAA04CB68863418173C125D1)
      return "8ca8cc2b-baa0-4cb6-8863-418173c125d1";
    if (code == HspcPatientVeteranMilitaryStatus._4EB9A0A1333448DF084DCEF37C9AC0D3)
      return "4eb9a0a1-3334-48df-084d-cef37c9ac0d3";
    if (code == HspcPatientVeteranMilitaryStatus._70A6818301A445B1A5E649A13E221713)
      return "70a68183-01a4-45b1-a5e6-49a13e221713";
    if (code == HspcPatientVeteranMilitaryStatus._189E9AF6BCBF47E2ACEE4E5C50F54BA6)
      return "189e9af6-bcbf-47e2-acee-4e5c50f54ba6";
    if (code == HspcPatientVeteranMilitaryStatus.BAB7B911E3CF426F95D60637D7091E07)
      return "bab7b911-e3cf-426f-95d6-0637d7091e07";
    if (code == HspcPatientVeteranMilitaryStatus._2B98CEC8B29D45459350A2B54DBD2FE7)
      return "2b98cec8-b29d-4545-9350-a2b54dbd2fe7";
    if (code == HspcPatientVeteranMilitaryStatus._617936ECD6A74642A1D8B27BF8735F9D)
      return "617936ec-d6a7-4642-a1d8-b27bf8735f9d";
    if (code == HspcPatientVeteranMilitaryStatus._40D86A6267A949C68F43C1A9EF51689B)
      return "40d86a62-67a9-49c6-8f43-c1a9ef51689b";
    if (code == HspcPatientVeteranMilitaryStatus.CA02026A3EBF4FB409E002D89C931643)
      return "ca02026a-3ebf-4fb4-09e0-02d89c931643";
    if (code == HspcPatientVeteranMilitaryStatus._75F4440AEC3747FEB26D39C09C204676)
      return "75f4440a-ec37-47fe-b26d-39c09c204676";
    return "?";
  }

    public String toSystem(HspcPatientVeteranMilitaryStatus code) {
      return code.getSystem();
      }

}

