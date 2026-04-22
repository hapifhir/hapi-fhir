/*
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IFhirResourceDaoConceptMap<T extends IBaseResource> extends IFhirResourceDao<T> {
	TranslateConceptResults translate(TranslationRequest theTranslationRequest, RequestDetails theRequestDetails);

	/**
	 * Operation: <code>ConceptMap/$hapi.fhir.add-mapping</code>
	 *
	 * @since 8.6.0
	 */
	IBaseOperationOutcome addMapping(AddMappingRequest theRequest, RequestDetails theRequestDetails);

	/**
	 * Operation: <code>ConceptMap/$hapi.fhir.remove-mapping</code>
	 *
	 * @since 8.6.0
	 */
	IBaseOperationOutcome removeMapping(RemoveMappingRequest theRequest, RequestDetails theRequestDetails);

	class RemoveMappingRequest {
		private String myConceptMapUri;
		private String myConceptMapVersion;
		private String mySourceSystem;
		private String mySourceCode;
		private String myTargetSystem;
		private String myTargetCode;
		private String mySourceSystemVersion;
		private String myTargetSystemVersion;

		public String getConceptMapUri() {
			return myConceptMapUri;
		}

		public void setConceptMapUri(String theConceptMapUri) {
			myConceptMapUri = theConceptMapUri;
		}

		public void setConceptMapVersion(String theConceptMapVersion) {
			myConceptMapVersion = theConceptMapVersion;
		}

		public String getConceptMapVersion() {
			return myConceptMapVersion;
		}

		public String getSourceSystem() {
			return mySourceSystem;
		}

		public void setSourceSystem(String theSourceSystem) {
			mySourceSystem = theSourceSystem;
		}

		public String getSourceCode() {
			return mySourceCode;
		}

		public void setSourceCode(String theSourceCode) {
			mySourceCode = theSourceCode;
		}

		public String getTargetSystem() {
			return myTargetSystem;
		}

		public void setTargetSystem(String theTargetSystem) {
			myTargetSystem = theTargetSystem;
		}

		public String getTargetCode() {
			return myTargetCode;
		}

		public void setTargetCode(String theTargetCode) {
			myTargetCode = theTargetCode;
		}

		public void setSourceSystemVersion(String theSourceSystemVersion) {
			mySourceSystemVersion = theSourceSystemVersion;
		}

		public String getSourceSystemVersion() {
			return mySourceSystemVersion;
		}

		public void setTargetSystemVersion(String theTargetSystemVersion) {
			myTargetSystemVersion = theTargetSystemVersion;
		}

		public String getTargetSystemVersion() {
			return myTargetSystemVersion;
		}
	}

	class AddMappingRequest extends RemoveMappingRequest {
		private String mySourceDisplay;
		private String myTargetDisplay;
		private String myEquivalence;

		public String getTargetDisplay() {
			return myTargetDisplay;
		}

		public void setTargetDisplay(String theTargetDisplay) {
			myTargetDisplay = theTargetDisplay;
		}

		public String getSourceDisplay() {
			return mySourceDisplay;
		}

		public void setSourceDisplay(String theSourceDisplay) {
			mySourceDisplay = theSourceDisplay;
		}

		public String getEquivalence() {
			return myEquivalence;
		}

		public void setEquivalence(String theEquivalence) {
			myEquivalence = theEquivalence;
		}
	}
}
