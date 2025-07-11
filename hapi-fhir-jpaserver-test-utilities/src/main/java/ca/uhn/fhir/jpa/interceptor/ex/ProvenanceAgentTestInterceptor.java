/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.interceptor.ex;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.IProvenanceAgentsPointcutParameter;
import ca.uhn.fhir.model.api.IProvenanceAgent;

import java.util.List;

/**
 * A test interceptor that allows you return ProvenanceAgents you provide
 */
public class ProvenanceAgentTestInterceptor {
	private final List<IProvenanceAgent> myAgents;

	public ProvenanceAgentTestInterceptor(List<IProvenanceAgent> theAgents) {
		myAgents = theAgents;
	}

	@Hook(Pointcut.PROVENANCE_AGENTS)
	public void getProvenanceAgent(IProvenanceAgentsPointcutParameter params) {
		myAgents.forEach(params::addProvenanceAgent);
	}
}
