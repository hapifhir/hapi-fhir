/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Parameters;
import org.opencds.cqf.fhir.api.Repository;

public class CdsCrServiceR5 implements ICdsCrService {
	private final Repository myRepository;
	private final IIdType myPlanDefinitionId;
	private Bundle myResponseBundle;

	public CdsCrServiceR5(IIdType thePlanDefinitionId, Repository theRepository) {
		myPlanDefinitionId = thePlanDefinitionId;
		myRepository = theRepository;
	}

	public FhirVersionEnum getFhirVersion() {
		return FhirVersionEnum.R5;
	}

	public Repository getRepository() {
		return myRepository;
	}

	public Parameters encodeParams(CdsServiceRequestJson theJson) {
		var retVal = new Parameters();
		return retVal;
	}

	public CdsServiceResponseJson encodeResponse(Object theResponse) {
		var retVal = new CdsServiceResponseJson();
		return retVal;
	}
}
