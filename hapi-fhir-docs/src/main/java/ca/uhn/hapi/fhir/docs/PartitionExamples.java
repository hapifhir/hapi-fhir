package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@SuppressWarnings("InnerClassMayBeStatic")
public class PartitionExamples {

	public void multitenantServer() {

	}


	// START SNIPPET: partitionInterceptorRequestPartition
	@Interceptor
	public class RequestTenantPartitionInterceptor {

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId PartitionIdentifyCreate(ServletRequestDetails theRequestDetails) {
			return extractPartitionIdFromRequest(theRequestDetails);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId PartitionIdentifyRead(ServletRequestDetails theRequestDetails) {
			return extractPartitionIdFromRequest(theRequestDetails);
		}

		private RequestPartitionId extractPartitionIdFromRequest(ServletRequestDetails theRequestDetails) {
			// We will use the tenant ID that came from the request as the partition name
			String tenantId = theRequestDetails.getTenantId();
			return RequestPartitionId.fromPartitionName(tenantId);
		}

	}
	// END SNIPPET: partitionInterceptorRequestPartition


	// START SNIPPET: partitionInterceptorHeaders
	@Interceptor
	public class CustomHeaderBasedPartitionInterceptor {

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId PartitionIdentifyCreate(ServletRequestDetails theRequestDetails) {
			String partitionName = theRequestDetails.getHeader("X-Partition-Name");
			return RequestPartitionId.fromPartitionName(partitionName);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId PartitionIdentifyRead(ServletRequestDetails theRequestDetails) {
			String partitionName = theRequestDetails.getHeader("X-Partition-Name");
			return RequestPartitionId.fromPartitionName(partitionName);
		}

	}
	// END SNIPPET: partitionInterceptorHeaders


	// START SNIPPET: partitionInterceptorResourceContents
	@Interceptor
	public class ResourceTypePartitionInterceptor {

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId PartitionIdentifyCreate(IBaseResource theResource) {
			if (theResource instanceof Patient) {
				return RequestPartitionId.fromPartitionName("PATIENT");
			} else if (theResource instanceof Observation) {
					return RequestPartitionId.fromPartitionName("OBSERVATION");
			} else {
				return RequestPartitionId.fromPartitionName("OTHER");
			}
		}

	}
	// END SNIPPET: partitionInterceptorResourceContents


	// START SNIPPET: partitionInterceptorReadAllPartitions
	@Interceptor
	public class PartitionInterceptorReadAllPartitions {

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId readPartition() {
			return RequestPartitionId.allPartitions();
		}

	}
	// END SNIPPET: partitionInterceptorReadAllPartitions


	// START SNIPPET: partitionInterceptorReadBasedOnScopes
	@Interceptor
	public class PartitionInterceptorReadPartitionsBasedOnScopes {

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId readPartition(ServletRequestDetails theRequest) {

			HttpServletRequest servletRequest = theRequest.getServletRequest();
			Set<String> approvedScopes = (Set<String>) servletRequest.getAttribute("ca.cdr.servletattribute.session.oidc.approved_scopes");

			String partition = approvedScopes
				.stream()
				.filter(t->t.startsWith("partition-"))
				.map(t->t.substring("partition-".length()))
				.findFirst()
				.orElseThrow(()->new InvalidRequestException("No partition scopes found in request"));
			return RequestPartitionId.fromPartitionName(partition);

		}

	}
	// END SNIPPET: partitionInterceptorReadBasedOnScopes


	// START SNIPPET: multitenantServer
	public class MultitenantServer extends RestfulServer {

		@Autowired
		private PartitionSettings myPartitionSettings;

		@Override
		protected void initialize() {

			// Enable partitioning
			myPartitionSettings.setPartitioningEnabled(true);

			// Set the tenant identification strategy
			setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());

			// Use the tenant ID supplied by the tenant identification strategy
			// to serve as the partitioning ID
			registerInterceptor(new RequestTenantPartitionInterceptor());

			// ....Register some providers and other things....

		}
	}
	// END SNIPPET: multitenantServer


}
