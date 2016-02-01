package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ObservationCategoryEnumFactory implements EnumFactory<ObservationCategory> {

  public ObservationCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("social-history".equals(codeString))
      return ObservationCategory.SOCIALHISTORY;
    if ("vital-signs".equals(codeString))
      return ObservationCategory.VITALSIGNS;
    if ("imaging".equals(codeString))
      return ObservationCategory.IMAGING;
    if ("laboratory".equals(codeString))
      return ObservationCategory.LABORATORY;
    if ("procedure".equals(codeString))
      return ObservationCategory.PROCEDURE;
    if ("survey".equals(codeString))
      return ObservationCategory.SURVEY;
    if ("exam".equals(codeString))
      return ObservationCategory.EXAM;
    if ("therapy".equals(codeString))
      return ObservationCategory.THERAPY;
    throw new IllegalArgumentException("Unknown ObservationCategory code '"+codeString+"'");
  }

  public String toCode(ObservationCategory code) {
    if (code == ObservationCategory.SOCIALHISTORY)
      return "social-history";
    if (code == ObservationCategory.VITALSIGNS)
      return "vital-signs";
    if (code == ObservationCategory.IMAGING)
      return "imaging";
    if (code == ObservationCategory.LABORATORY)
      return "laboratory";
    if (code == ObservationCategory.PROCEDURE)
      return "procedure";
    if (code == ObservationCategory.SURVEY)
      return "survey";
    if (code == ObservationCategory.EXAM)
      return "exam";
    if (code == ObservationCategory.THERAPY)
      return "therapy";
    return "?";
  }

    public String toSystem(ObservationCategory code) {
      return code.getSystem();
      }

}

