package org.hl7.fhir.utilities;

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