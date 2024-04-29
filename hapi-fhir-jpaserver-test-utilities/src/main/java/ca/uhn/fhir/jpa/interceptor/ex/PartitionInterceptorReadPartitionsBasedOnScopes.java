/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

// This class is replicated in PartitionExamples.java -- Keep it up to date there too!!
@Interceptor
public class PartitionInterceptorReadPartitionsBasedOnScopes {

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId readPartition(ServletRequestDetails theRequest) {

		HttpServletRequest servletRequest = theRequest.getServletRequest();
		Set<String> approvedScopes =
				(Set<String>) servletRequest.getAttribute("ca.cdr.servletattribute.session.oidc.approved_scopes");

		String partition = approvedScopes.stream()
				.filter(t -> t.startsWith("partition-"))
				.map(t -> t.substring("partition-".length()))
				.findFirst()
				.orElseThrow(() -> new InvalidRequestException("No partition scopes found in request"));
		return RequestPartitionId.fromPartitionName(partition);
	}
}
