package org.hl7.fhir.utilities;

import java.util.Date;

public class TranslatingUtilities {

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

  protected boolean hasTranslator() {
    return translator != null;
  }

  public String toStr(int value) {
    return hasTranslator() ? translator.toStr(value) : Integer.toString(value);
  }
  
  public String toStr(Date value) {
    return hasTranslator() ? translator.toStr(value) : value.toString();
  }
  
  protected String translate(String context, String value, Object... args) {
    if (hasTranslator()) {
      String alt = translator.translate(context, value);
      if (alt != null)
        value = alt;
    }
    return String.format(value, args);      
  }
  
  
}
