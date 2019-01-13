package org.hl7.fhir.dstu3.utils;

/*-
 * #%L
 * org.hl7.fhir.dstu3
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



import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.PrimitiveType;

public class TranslatingUtilities extends org.hl7.fhir.utilities.TranslatingUtilities {

  public interface TranslationServices extends org.hl7.fhir.utilities.TranslationServices {
    String gt(@SuppressWarnings("rawtypes") PrimitiveType value);
    String egt(@SuppressWarnings("rawtypes") Enumeration<? extends Enum> value);
  }

  public String gt(@SuppressWarnings("rawtypes") PrimitiveType value) {
    return hasTranslator() ? ((TranslationServices) getTranslator()).gt(value) : value.asStringValue();
  }

  public String egt(@SuppressWarnings("rawtypes") Enumeration<? extends Enum> value) {
    return hasTranslator() ? ((TranslationServices) getTranslator()).egt(value) : value.asStringValue();
  }


}
