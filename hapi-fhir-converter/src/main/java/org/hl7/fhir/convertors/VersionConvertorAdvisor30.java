package org.hl7.fhir.convertors;

import org.hl7.fhir.exceptions.FHIRException;

public interface VersionConvertorAdvisor30 {
  boolean ignoreEntry(org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent src);

  // called ?
  org.hl7.fhir.instance.model.Resource convert(org.hl7.fhir.dstu3.model.Resource resource) throws FHIRException;

  // called when an r2 value set has a codeSystem in it
  void handleCodeSystem(org.hl7.fhir.dstu3.model.CodeSystem tgtcs, org.hl7.fhir.dstu3.model.ValueSet source);

  org.hl7.fhir.dstu3.model.CodeSystem getCodeSystem(org.hl7.fhir.dstu3.model.ValueSet src);
}
