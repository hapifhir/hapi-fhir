/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.hapi.fhir.cdshooks.api;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServicesJson;

import java.util.function.Function;

/**
 * This registry holds all CDS Hooks services registered with the server.
 */
public interface ICdsServiceRegistry {
	/**
	 * This is the json returned by calling https://example.com/cds-services
	 *
	 * @return a list of CDS Hooks service descriptors
	 */
	CdsServicesJson getCdsServicesJson();

	/**
	 * This is the REST method available at https://example.com/cds-services/{theServiceId}
	 *
	 * @param theServiceId the id of the service to be called
	 * @param theCdsServiceRequestJson the service request
	 * @return the service response
	 */
	CdsServiceResponseJson callService(String theServiceId, CdsServiceRequestJson theCdsServiceRequestJson);

	/**
	 * This is the REST method available at https://example.com/cds-services/{theServiceId}/feedback
	 *
	 * @param theServiceId the id of the service that feedback is being sent for
	 * @param theCdsServiceFeedbackJson the request
	 * @return the response
	 */
	String callFeedback(String theServiceId, CdsServiceFeedbackJson theCdsServiceFeedbackJson);

	/**
	 * Register a new CDS Service with the endpoint.
	 *
	 * @param theServiceId                   the id of the service
	 * @param theServiceFunction             the function that will be called to invoke the service
	 * @param theCdsServiceJson              the service descriptor
	 * @param theAllowAutoFhirClientPrefetch Whether to allow the server to automatically prefetch resources
	 * @param theModuleId                    the moduleId where the service is registered
	 */

	void registerService(String theServiceId, Function<CdsServiceRequestJson, CdsServiceResponseJson> theServiceFunction, CdsServiceJson theCdsServiceJson, boolean theAllowAutoFhirClientPrefetch, String theModuleId);

	/**
	 * Remove registered CDS service with the service ID, only removes dynamically registered service
	 *
	 * @param theServiceId the id of the service to be removed
	 */
	void unregisterService(String theServiceId, String theModuleId);
}
