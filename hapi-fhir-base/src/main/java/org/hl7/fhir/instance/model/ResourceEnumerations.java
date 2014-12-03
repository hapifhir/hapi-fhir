package org.hl7.fhir.instance.model;

public class ResourceEnumerations {

  @SuppressWarnings("rawtypes")
  public static EnumFactory getEnumFactory(Class<? extends Enum> clss) {
    if (clss == HumanName.NameUse.class)
      return new HumanName.NameUseEnumFactory();
    if (clss == Observation.ObservationReliability.class)
      return new Observation.ObservationReliabilityEnumFactory();
    if (clss == Observation.ObservationStatus.class)
      return new Observation.ObservationStatusEnumFactory();
    return null;
  }

}
