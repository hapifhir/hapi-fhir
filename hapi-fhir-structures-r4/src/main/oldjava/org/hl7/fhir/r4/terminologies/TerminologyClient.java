package org.hl7.fhir.r4.terminologies;

import java.util.Map;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.HTMLClientLogger;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.TerminologyCapabilities;
import org.hl7.fhir.r4.model.ValueSet;

public interface TerminologyClient {
  public String getAddress();
  public TerminologyCapabilities getTerminologyCapabilities() throws FHIRException;
  public ValueSet expandValueset(ValueSet vs, Parameters p, Map<String, String> params) throws FHIRException;
  public Parameters validateCS(Parameters pin) throws FHIRException;
  public Parameters validateVS(Parameters pin) throws FHIRException;
  public void setTimeout(int i) throws FHIRException;
  public void setLogger(HTMLClientLogger txLog) throws FHIRException;
  public CapabilityStatement getCapabilitiesStatementQuick() throws FHIRException;
  public Parameters lookupCode(Map<String, String> params) throws FHIRException;
}
