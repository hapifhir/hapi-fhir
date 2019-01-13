package org.hl7.fhir.utilities;

/*-
 * #%L
 * org.hl7.fhir.utilities
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


import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface TranslationServices {
  /**
   * General translation functionality - given a string, translate it to a different language
   * 
   * @param context - for debugging purposes
   * @param value - the string to translate
   * @param targetLang - the target language to translate to. 
   * 
   * @return the translated string, or value if no translation is found
   */
  String translate(String context, String value, String targetLang);

  /**
   * General translation functionality - given a string, translate it to a different language, but also perform String.format on the outcome.
   * 
   * @param contest
   * @param lang
   * @param string2
   * @param args
   * @return
   */
  String translateAndFormat(String contest, String lang, String string2, Object... args);

  /** 
   * equivalent to the general translation operation, but the context that provides the transations specifies the target language
   *  
   * @param context
   * @param value
   * @return
   */
  String translate(String context, String value);

  /**
   * Get a list of all translations available for a phrase
   * 
   * @param value
   * @return
   */
  Map<String, String> translations(String value);

  /** 
   * localization for converting a decimal to language specific representation
   * 
   * @param value
   * @return
   */
  String toStr(float value);

  /** 
   * localization for converting a date to language specific representation
   * 
   * @param value
   * @return
   */
  String toStr(Date value);

  /**
   * get a list of translation codes by category
   * @param category
   * @return
   */
  Set<String> listTranslations(String category);

}
