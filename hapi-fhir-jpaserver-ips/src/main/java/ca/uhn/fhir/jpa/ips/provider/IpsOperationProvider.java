/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.ips.provider;

import ca.uhn.fhir.jpa.ips.generator.IIpsGeneratorSvc;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;

public class IpsOperationProvider {

	private final IIpsGeneratorSvc myIpsGeneratorSvc;

	/**
	 * Constructor
	 */
	public IpsOperationProvider(IIpsGeneratorSvc theIpsGeneratorSvc) {
		myIpsGeneratorSvc = theIpsGeneratorSvc;
	}

	/**
	 * Patient/123/$summary
	 * <p>
	 * Note that not all parameters from the official specification are yet supported. See
	 * <a href="http://build.fhir.org/ig/HL7/fhir-ips/OperationDefinition-summary.html>http://build.fhir.org/ig/HL7/fhir-ips/OperationDefinition-summary.html</a>
	 */
	@Operation(
			name = JpaConstants.OPERATION_SUMMARY,
			idempotent = true,
			bundleType = BundleTypeEnum.DOCUMENT,
			typeName = "Patient",
			canonicalUrl = JpaConstants.SUMMARY_OPERATION_URL)
	public IBaseBundle patientInstanceSummary(@IdParam IIdType thePatientId, RequestDetails theRequestDetails) {

		return myIpsGeneratorSvc.generateIps(theRequestDetails, thePatientId);
	}

	/**
	 * /Patient/$summary?identifier=foo|bar
	 * <p>
	 * Note that not all parameters from the official specification are yet supported. See
	 * <a href="http://build.fhir.org/ig/HL7/fhir-ips/OperationDefinition-summary.html>http://build.fhir.org/ig/HL7/fhir-ips/OperationDefinition-summary.html</a>
	 */
	@Operation(
			name = JpaConstants.OPERATION_SUMMARY,
			idempotent = true,
			bundleType = BundleTypeEnum.DOCUMENT,
			typeName = "Patient",
			canonicalUrl = JpaConstants.SUMMARY_OPERATION_URL)
	public IBaseBundle patientTypeSummary(
			@Description(
							shortDefinition =
									"When the logical id of the patient is not used, servers MAY choose to support patient selection based on provided identifier")
					@OperationParam(name = "identifier", min = 0, max = 1)
					TokenParam thePatientIdentifier,
			RequestDetails theRequestDetails) {
		return myIpsGeneratorSvc.generateIps(theRequestDetails, thePatientIdentifier);
	}
}
