//
//
//

package org.hl7.fhir.r4.utils.transform.deserializer;


public enum FhirMapInputModes
{
  /**
  * Fhir mapper .
  *
  * Unset value
  */
  NotSet,
  /**
  * Source mode
  */
  Source,
  /**
  * Target mode
  */
  Target;

  public String getValue(){
    switch (this) {
      case Source:
        return "source";
      case Target:
        return "target";
      default:
        return "?";
    }
  }
}
