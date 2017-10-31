//
//
//

package org.hl7.fhir.r4.utils.transform.deserializer;


import java.util.Objects;

public enum FhirMapUseNames
{
  /**
  'source'
  */
  NotSet,
  
  /**
  'source'
  */
  Source,

  /**
  'target'
  */
  Target,

  /**
  'queried'
  */
  Queried,

  /**
  'produced'
  */
  Produced;

  public static final int SIZE = java.lang.Integer.SIZE;

  public String getValue()
  {
    switch (this) {
      case Source:
      return "source";
      case Queried:
      return "queried";
      case Target:
      return "target";
      case Produced:
      return "produced";
      default:
      return "?";
    }
  }




  public static FhirMapUseNames forValue(int value)
  {
    return values()[value];
  }

  public static FhirMapUseNames forValue(String value)
  {

    if (Objects.equals(value, "Source")){
      return FhirMapUseNames.Source;
    }
    else if (Objects.equals(value, "Target")){
      return FhirMapUseNames.Target;
    }
    else if (Objects.equals(value, "Queried")){
      return FhirMapUseNames.Queried;
    }
    else if (Objects.equals(value, "Produced")){
      return FhirMapUseNames.Produced;
    }
    else
    return FhirMapUseNames.NotSet;
  }
}
