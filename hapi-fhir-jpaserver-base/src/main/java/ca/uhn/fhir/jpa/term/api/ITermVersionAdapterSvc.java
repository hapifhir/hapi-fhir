package ca.uhn.fhir.jpa.term.api;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

/**
 * This interface is used to handle differences in versions of FHIR for the terminology
 * server. It is really just an internal interface used by the
 * {@link ITermReadSvc terminology read service}.
 */
public interface ITermVersionAdapterSvc {

	default IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource) {
		return createOrUpdateCodeSystem(theCodeSystemResource, null);
	}

	IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource, RequestDetails theRequestDetails);

	void createOrUpdateConceptMap(ConceptMap theNextConceptMap);

	void createOrUpdateValueSet(ValueSet theValueSet);

}
