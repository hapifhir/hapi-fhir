package org.hl7.fhir.convertors;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;

public interface VersionConvertorAdvisor {
  boolean ignoreEntry(org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent src);

  // called ?
  org.hl7.fhir.instance.model.Resource convert(org.hl7.fhir.dstu3.model.Resource resource) throws FHIRException;

  // called when an r2 value set has a codeSystem in it
  void handleCodeSystem(CodeSystem tgtcs, ValueSet source);

  CodeSystem getCodeSystem(ValueSet src);
}