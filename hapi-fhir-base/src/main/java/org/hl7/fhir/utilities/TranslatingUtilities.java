package org.hl7.fhir.utilities;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

public class TranslatingUtilities {

  public interface TranslationServices {
    String translate(String context, String value);
    String translate(String context, String value, Object... args);
    String toStr(int value);
    String toStr(Date value);
  }

  private TranslationServices translator;

  public TranslationServices getTranslator() {
    return translator;
  }

  public void setTranslator(TranslationServices translator) {
    this.translator = translator;
  }
  
  protected String translate(String context, String value) {
    return hasTranslator() ? translator.translate(context, value) : value;
  }

  protected String translate(String context, String value, Object... args) {
    return hasTranslator() ? translator.translate(context, value, args) : String.format(value, args);
  }
  
  protected boolean hasTranslator() {
    return translator != null;
  }

  public String toStr(int value) {
    return hasTranslator() ? translator.toStr(value) : Integer.toString(value);
  }
  
  public String toStr(Date value) {
    return hasTranslator() ? translator.toStr(value) : value.toString();
  }
}
