package ca.uhn.fhir.jpa.term;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

public class TermVersionAdapterSvcDstu2 implements ITermVersionAdapterSvc {

	@Override
	public IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException(Msg.code(882));
	}

	@Override
	public void createOrUpdateConceptMap(ConceptMap theNextConceptMap) {
		throw new UnsupportedOperationException(Msg.code(883));
	}

	@Override
	public void createOrUpdateValueSet(ValueSet theValueSet) {
		throw new UnsupportedOperationException(Msg.code(884));
	}

}
