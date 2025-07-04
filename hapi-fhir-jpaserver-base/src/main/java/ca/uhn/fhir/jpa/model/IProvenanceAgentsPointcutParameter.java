/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.model;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;

/**
 * This interface is used to pass parameters to the {@link Pointcut#PROVENANCE_AGENTS} pointcut.
 * It allows interceptors to retrieve input parameters and provide provenance agents as an output parameter.
 */
public interface IProvenanceAgentsPointcutParameter {
	List<IProvenanceAgent> getProvenanceAgents();

	IProvenanceAgentsPointcutParameter setProvenanceAgents(List<IProvenanceAgent> theProvenanceAgents);

	void addProvenanceAgent(IProvenanceAgent theProvenanceAgent);

	RequestDetails getRequestDetails();

	IProvenanceAgentsPointcutParameter setRequestDetails(RequestDetails myRequestDetails);
}
