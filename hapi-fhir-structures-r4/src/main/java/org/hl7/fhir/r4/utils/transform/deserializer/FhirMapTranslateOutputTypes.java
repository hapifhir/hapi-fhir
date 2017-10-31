//
//
//

package org.hl7.fhir.r4.utils.transform.deserializer;


public enum FhirMapTranslateOutputTypes
{
  /**
  FhirMapTranslateOutputTypes.Code
  */
  Code,

  /**
  FhirMapTranslateOutputTypes.System,
  */
  System,

  /**
  FhirMapTranslateOutputTypes.Display
  */
  Display,

  /**
  FhirMapTranslateOutputTypes.Coding
  */
  Coding,

  /**
  FhirMapTranslateOutputTypes.CodeableConcept
  */
  CodeableConcept;

  public static final int SIZE = java.lang.Integer.SIZE;

  public String getValue()
  {
    switch (this){
      case Code: return "Code";
      case Coding: return "Coding";
      case System: return "System";
      case Display: return "Display";
      case CodeableConcept: return "CodeableConcept";
      default: return "?";
    }
  }

  public static FhirMapTranslateOutputTypes forValue(int value)
  {
    return values()[value];
  }
}
