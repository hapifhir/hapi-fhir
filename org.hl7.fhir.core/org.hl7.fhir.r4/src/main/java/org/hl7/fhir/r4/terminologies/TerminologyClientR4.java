package org.hl7.fhir.r4.terminologies;

/*-
 * #%L
 * org.hl7.fhir.r4
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.net.URISyntaxException;
import java.util.Map;

import org.hl7.fhir.r4.context.HTMLClientLogger;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.TerminologyCapabilities;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.utils.client.FHIRToolingClient;

public class TerminologyClientR4 implements TerminologyClient {

  private FHIRToolingClient client;
  
  public TerminologyClientR4(String address) throws URISyntaxException {
    client = new FHIRToolingClient(address);
  }

  @Override
  public TerminologyCapabilities getTerminologyCapabilities() {
    return client.getTerminologyCapabilities();
  }

  @Override
  public String getAddress() {
    return client.getAddress();
  }

  @Override
  public ValueSet expandValueset(ValueSet vs, Parameters p, Map<String, String> params) {
    return client.expandValueset(vs, p, params);
  }

  @Override
  public Parameters validateCS(Parameters pin) {
    return client.operateType(CodeSystem.class, "validate-code", pin);
  }

  @Override
  public Parameters validateVS(Parameters pin) {
    return client.operateType(ValueSet.class, "validate-code", pin);
  }

  @Override
  public void setTimeout(int i) {
    client.setTimeout(i);    
  }

  @Override
  public void setLogger(HTMLClientLogger txLog) {
    client.setLogger(txLog);
  }

  @Override
  public CapabilityStatement getCapabilitiesStatementQuick() {
    return client.getCapabilitiesStatementQuick();
  }

  @Override
  public Parameters lookupCode(Map<String, String> params) {
    return client.lookupCode(params);
  }

}
