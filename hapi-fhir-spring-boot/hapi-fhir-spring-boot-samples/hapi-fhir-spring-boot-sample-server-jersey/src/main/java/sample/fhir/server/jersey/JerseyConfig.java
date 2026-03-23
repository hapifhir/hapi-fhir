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
package sample.fhir.server.jersey;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import sample.fhir.server.jersey.provider.JerseyConformanceProvider;
import sample.fhir.server.jersey.provider.PatientResourceProvider;

// Created by claude-sonnet-4-6
@Component
@ApplicationPath("/fhir")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(JerseyConformanceProvider.class);
		register(PatientResourceProvider.class);
	}
}
