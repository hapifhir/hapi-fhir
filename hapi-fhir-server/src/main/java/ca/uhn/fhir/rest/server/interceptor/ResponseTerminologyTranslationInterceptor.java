/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.rest.server.interceptor.InterceptorOrders.RESPONSE_TERMINOLOGY_TRANSLATION_INTERCEPTOR;

/**
 * This interceptor leverages ConceptMap resources stored in the repository to automatically map
 * terminology from one CodeSystem to another at runtime, in resources that are being
 * returned by the server.
 * <p>
 * Mappings are applied only if they are explicitly configured in the interceptor via
 * the {@link #addMappingSpecification(String, String)} method.
 * </p>
 *
 * @since 5.4.0
 */
public class ResponseTerminologyTranslationInterceptor extends BaseResponseTerminologyInterceptor {

	private final ResponseTerminologyTranslationSvc myResponseTerminologyTranslationSvc;

	/**
	 * Constructor
	 *
	 * @param theValidationSupport The validation support module
	 */
	public ResponseTerminologyTranslationInterceptor(
			IValidationSupport theValidationSupport,
			ResponseTerminologyTranslationSvc theResponseTerminologyTranslationSvc) {
		super(theValidationSupport);
		myResponseTerminologyTranslationSvc = theResponseTerminologyTranslationSvc;
	}

	/**
	 * Adds a mapping specification using only a source and target CodeSystem URL. Any mappings specified using
	 * this URL
	 *
	 * @param theSourceCodeSystemUrl The source CodeSystem URL
	 * @param theTargetCodeSystemUrl The target CodeSystem URL
	 */
	public void addMappingSpecification(String theSourceCodeSystemUrl, String theTargetCodeSystemUrl) {
		myResponseTerminologyTranslationSvc.addMappingSpecification(theSourceCodeSystemUrl, theTargetCodeSystemUrl);
	}

	/**
	 * Clear all mapping specifications
	 */
	public void clearMappingSpecifications() {
		myResponseTerminologyTranslationSvc.clearMappingSpecifications();
	}

	public Map<String, String> getMappingSpecifications() {
		return myResponseTerminologyTranslationSvc.getMappingSpecifications();
	}

	@Hook(value = Pointcut.SERVER_OUTGOING_RESPONSE, order = RESPONSE_TERMINOLOGY_TRANSLATION_INTERCEPTOR)
	public void handleResource(RequestDetails theRequestDetails, IBaseResource theResource) {
		List<IBaseResource> resources = toListForProcessing(theRequestDetails, theResource);
		myResponseTerminologyTranslationSvc.processResourcesForTerminologyTranslation(resources);
	}
}
