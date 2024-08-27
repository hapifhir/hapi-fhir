/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IIpsGeneratorSvc {

	/**
	 * Generates an IPS document and returns the complete document bundle
	 * for the given patient by ID
	 */
	IBaseBundle generateIps(RequestDetails theRequestDetails, IIdType thePatientId, String theProfile);

	/**
	 * Generates an IPS document and returns the complete document bundle
	 * for the given patient by identifier
	 */
	IBaseBundle generateIps(RequestDetails theRequestDetails, TokenParam thePatientIdentifier, String theProfile);
}
