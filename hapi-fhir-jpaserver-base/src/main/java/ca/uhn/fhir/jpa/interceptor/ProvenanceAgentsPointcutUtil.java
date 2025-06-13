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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.IProvenanceAgentsPointcutParameter;
import ca.uhn.fhir.jpa.model.ProvenanceAgentsPointcutParameter;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;

import java.util.Collections;
import java.util.List;

/**
 *  Utility class for PROVENANCE_AGENTS pointcut.
 */
public class ProvenanceAgentsPointcutUtil {

	private ProvenanceAgentsPointcutUtil() {}

	public static List<IProvenanceAgent> ifHasCallTheHooks(
			RequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster) {
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(theInterceptorBroadcaster, theRequestDetails);

		if (compositeBroadcaster.hasHooks(Pointcut.PROVENANCE_AGENTS)) {

			ProvenanceAgentsPointcutParameter agentParam = new ProvenanceAgentsPointcutParameter();
			agentParam.setRequestDetails(theRequestDetails);

			HookParams hookParams = new HookParams().add(IProvenanceAgentsPointcutParameter.class, agentParam);
			compositeBroadcaster.callHooks(Pointcut.PROVENANCE_AGENTS, hookParams);

			List<IProvenanceAgent> agents = agentParam.getProvenanceAgents();
			if (agents.isEmpty()) {
				throw new InternalErrorException(Msg.code(2723)
						+ "No Provenance Agent was provided by any interceptor for Pointcut.PROVENANCE_AGENTS");
			}
			return agents;
		}
		return Collections.emptyList();
	}
}
