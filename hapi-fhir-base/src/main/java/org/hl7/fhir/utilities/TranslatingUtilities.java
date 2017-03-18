package org.hl7.fhir.utilities;

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
