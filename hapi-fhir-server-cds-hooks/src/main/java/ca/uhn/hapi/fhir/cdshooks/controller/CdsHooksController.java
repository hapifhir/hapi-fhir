/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.hapi.fhir.cdshooks.api.ICdsServiceRegistry;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServicesJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = CdsHooksController.BASE)
public class CdsHooksController {
	static final String BASE = "/cds-services";
	private final ICdsServiceRegistry myCdsServiceRegistry;

	/**
	 * Constructor
	 */
	public CdsHooksController(ICdsServiceRegistry theCdsServiceRegistry) {
		super();
		myCdsServiceRegistry = theCdsServiceRegistry;
	}

	/**
	 * Get the list of CDS Hooks service descriptors.
	 * This method is idempotent.
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(
			path = "",
			method = {RequestMethod.GET},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CdsServicesJson> cdsServices() {
		CdsServicesJson response = myCdsServiceRegistry.getCdsServicesJson();
		return ResponseEntity.status(200)
				.contentType(MediaType.APPLICATION_JSON)
				.body(response);
	}

	/**
	 * Call a specific CDS-Hook service
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(
			path = "{cds_hook}",
			method = {RequestMethod.POST},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CdsServiceResponseJson> cdsServiceRequest(
			@PathVariable("cds_hook") String theCdsHook, @RequestBody CdsServiceRequestJson theCdsServiceRequestJson) {
		CdsServiceResponseJson response = myCdsServiceRegistry.callService(theCdsHook, theCdsServiceRequestJson);
		return ResponseEntity.status(200)
				.contentType(MediaType.APPLICATION_JSON)
				.body(response);
	}

	/**
	 * Here is the description of the CDS Hooks feedback method from the CDS Hooks specification:
	 * <p>
	 * Once a CDS Hooks service responds to a hook by returning a card, the service has no further interaction with the CDS client. The acceptance of a suggestion or rejection of a card is valuable information to enable a service to improve its behavior towards the goal of the end-user having a positive and meaningful experience with the CDS. A feedback endpoint enables suggestion tracking & analytics.
	 * <p>
	 * Upon receiving a card, a user may accept its suggestions, ignore it entirely, or dismiss it with or without an override reason. Note that while one or more suggestions can be accepted, an entire card is either ignored or overridden.
	 * <p>
	 * Typically, an end user may only accept (a suggestion), or override a card once; however, a card once ignored could later be acted upon. CDS Hooks does not specify the UI behavior of CDS clients, including the persistence of cards. CDS clients should faithfully report each of these distinct end-user interactions as feedback.
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(
			path = "{cds_hook}/feedback",
			method = {RequestMethod.POST},
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<CdsServiceFeedbackJson> cdsServiceFeedback(
			@PathVariable("cds_hook") String theCdsHook,
			@RequestBody CdsServiceFeedbackJson theCdsServiceFeedbackJson) {
		CdsServiceFeedbackJson response = myCdsServiceRegistry.callFeedback(theCdsHook, theCdsServiceFeedbackJson);
		return ResponseEntity.status(200)
				.contentType(MediaType.APPLICATION_JSON)
				.body(response);
	}
}
