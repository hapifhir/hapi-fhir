package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class V3EthnicityEnumFactory implements EnumFactory<V3Ethnicity> {

  public V3Ethnicity fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("2135-2".equals(codeString))
      return V3Ethnicity._21352;
    if ("2137-8".equals(codeString))
      return V3Ethnicity._21378;
    if ("2138-6".equals(codeString))
      return V3Ethnicity._21386;
    if ("2139-4".equals(codeString))
      return V3Ethnicity._21394;
    if ("2140-2".equals(codeString))
      return V3Ethnicity._21402;
    if ("2141-0".equals(codeString))
      return V3Ethnicity._21410;
    if ("2142-8".equals(codeString))
      return V3Ethnicity._21428;
    if ("2143-6".equals(codeString))
      return V3Ethnicity._21436;
    if ("2144-4".equals(codeString))
      return V3Ethnicity._21444;
    if ("2145-1".equals(codeString))
      return V3Ethnicity._21451;
    if ("2146-9".equals(codeString))
      return V3Ethnicity._21469;
    if ("2148-5".equals(codeString))
      return V3Ethnicity._21485;
    if ("2149-3".equals(codeString))
      return V3Ethnicity._21493;
    if ("2150-1".equals(codeString))
      return V3Ethnicity._21501;
    if ("2151-9".equals(codeString))
      return V3Ethnicity._21519;
    if ("2152-7".equals(codeString))
      return V3Ethnicity._21527;
    if ("2153-5".equals(codeString))
      return V3Ethnicity._21535;
    if ("2155-0".equals(codeString))
      return V3Ethnicity._21550;
    if ("2156-8".equals(codeString))
      return V3Ethnicity._21568;
    if ("2157-6".equals(codeString))
      return V3Ethnicity._21576;
    if ("2158-4".equals(codeString))
      return V3Ethnicity._21584;
    if ("2159-2".equals(codeString))
      return V3Ethnicity._21592;
    if ("2160-0".equals(codeString))
      return V3Ethnicity._21600;
    if ("2161-8".equals(codeString))
      return V3Ethnicity._21618;
    if ("2162-6".equals(codeString))
      return V3Ethnicity._21626;
    if ("2163-4".equals(codeString))
      return V3Ethnicity._21634;
    if ("2165-9".equals(codeString))
      return V3Ethnicity._21659;
    if ("2166-7".equals(codeString))
      return V3Ethnicity._21667;
    if ("2167-5".equals(codeString))
      return V3Ethnicity._21675;
    if ("2168-3".equals(codeString))
      return V3Ethnicity._21683;
    if ("2169-1".equals(codeString))
      return V3Ethnicity._21691;
    if ("2170-9".equals(codeString))
      return V3Ethnicity._21709;
    if ("2171-7".equals(codeString))
      return V3Ethnicity._21717;
    if ("2172-5".equals(codeString))
      return V3Ethnicity._21725;
    if ("2173-3".equals(codeString))
      return V3Ethnicity._21733;
    if ("2174-1".equals(codeString))
      return V3Ethnicity._21741;
    if ("2175-8".equals(codeString))
      return V3Ethnicity._21758;
    if ("2176-6".equals(codeString))
      return V3Ethnicity._21766;
    if ("2178-2".equals(codeString))
      return V3Ethnicity._21782;
    if ("2180-8".equals(codeString))
      return V3Ethnicity._21808;
    if ("2182-4".equals(codeString))
      return V3Ethnicity._21824;
    if ("2184-0".equals(codeString))
      return V3Ethnicity._21840;
    if ("2186-5".equals(codeString))
      return V3Ethnicity._21865;
    throw new IllegalArgumentException("Unknown V3Ethnicity code '"+codeString+"'");
  }

  public String toCode(V3Ethnicity code) {
    if (code == V3Ethnicity._21352)
      return "2135-2";
    if (code == V3Ethnicity._21378)
      return "2137-8";
    if (code == V3Ethnicity._21386)
      return "2138-6";
    if (code == V3Ethnicity._21394)
      return "2139-4";
    if (code == V3Ethnicity._21402)
      return "2140-2";
    if (code == V3Ethnicity._21410)
      return "2141-0";
    if (code == V3Ethnicity._21428)
      return "2142-8";
    if (code == V3Ethnicity._21436)
      return "2143-6";
    if (code == V3Ethnicity._21444)
      return "2144-4";
    if (code == V3Ethnicity._21451)
      return "2145-1";
    if (code == V3Ethnicity._21469)
      return "2146-9";
    if (code == V3Ethnicity._21485)
      return "2148-5";
    if (code == V3Ethnicity._21493)
      return "2149-3";
    if (code == V3Ethnicity._21501)
      return "2150-1";
    if (code == V3Ethnicity._21519)
      return "2151-9";
    if (code == V3Ethnicity._21527)
      return "2152-7";
    if (code == V3Ethnicity._21535)
      return "2153-5";
    if (code == V3Ethnicity._21550)
      return "2155-0";
    if (code == V3Ethnicity._21568)
      return "2156-8";
    if (code == V3Ethnicity._21576)
      return "2157-6";
    if (code == V3Ethnicity._21584)
      return "2158-4";
    if (code == V3Ethnicity._21592)
      return "2159-2";
    if (code == V3Ethnicity._21600)
      return "2160-0";
    if (code == V3Ethnicity._21618)
      return "2161-8";
    if (code == V3Ethnicity._21626)
      return "2162-6";
    if (code == V3Ethnicity._21634)
      return "2163-4";
    if (code == V3Ethnicity._21659)
      return "2165-9";
    if (code == V3Ethnicity._21667)
      return "2166-7";
    if (code == V3Ethnicity._21675)
      return "2167-5";
    if (code == V3Ethnicity._21683)
      return "2168-3";
    if (code == V3Ethnicity._21691)
      return "2169-1";
    if (code == V3Ethnicity._21709)
      return "2170-9";
    if (code == V3Ethnicity._21717)
      return "2171-7";
    if (code == V3Ethnicity._21725)
      return "2172-5";
    if (code == V3Ethnicity._21733)
      return "2173-3";
    if (code == V3Ethnicity._21741)
      return "2174-1";
    if (code == V3Ethnicity._21758)
      return "2175-8";
    if (code == V3Ethnicity._21766)
      return "2176-6";
    if (code == V3Ethnicity._21782)
      return "2178-2";
    if (code == V3Ethnicity._21808)
      return "2180-8";
    if (code == V3Ethnicity._21824)
      return "2182-4";
    if (code == V3Ethnicity._21840)
      return "2184-0";
    if (code == V3Ethnicity._21865)
      return "2186-5";
    return "?";
  }


}

