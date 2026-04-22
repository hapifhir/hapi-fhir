/*-
 * #%L
 * hapi-fhir-spring-boot-sample-server-jersey
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package sample.fhir.server.jersey.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsConformanceProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

// Created by claude-sonnet-4-6
@Component
@Path("")
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class JerseyConformanceProvider extends AbstractJaxRsConformanceProvider {

	private final PatientResourceProvider patientResourceProvider;

	public JerseyConformanceProvider(PatientResourceProvider patientResourceProvider) {
		super(
				FhirContext.forDstu3Cached(),
				"Spring Boot Jersey Sample",
				"hapi-fhir-spring-boot-sample-server-jersey",
				"1.0.0");
		this.patientResourceProvider = patientResourceProvider;
	}

	@Override
	protected synchronized void buildCapabilityStatement() {
		if (getUriInfo() == null) {
			return;
		}
		super.buildCapabilityStatement();
	}

	@Override
	protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
		ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> map = new ConcurrentHashMap<>();
		map.put(PatientResourceProvider.class, patientResourceProvider);
		map.put(JerseyConformanceProvider.class, this);
		return map;
	}
}
