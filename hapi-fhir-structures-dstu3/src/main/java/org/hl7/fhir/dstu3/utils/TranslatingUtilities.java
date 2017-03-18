package org.hl7.fhir.dstu3.utils;


import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.PrimitiveType;

public class TranslatingUtilities extends org.hl7.fhir.utilities.TranslatingUtilities {

  public interface TranslationServices extends org.hl7.fhir.utilities.TranslatingUtilities.TranslationServices {
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
