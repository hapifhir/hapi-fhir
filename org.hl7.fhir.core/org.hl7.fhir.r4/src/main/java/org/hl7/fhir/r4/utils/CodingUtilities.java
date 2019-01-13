package org.hl7.fhir.r4.utils;

/*-
 * #%L
 * org.hl7.fhir.r4
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


import java.util.List;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.TestScript.TestActionComponent;

public class CodingUtilities {

  public static boolean matches(Coding coding, String system, String code) {
    if (coding == null)
      return false;
    return code.equals(coding.getCode()) && system.equals(coding.getSystem());
  }

  public static String present(Coding coding) {
    if (coding == null)
      return "";
    return coding.getSystem()+"::"+coding.getCode();
  }

}
