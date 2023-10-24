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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.fhir.api.Repository;

public class CdsCrUtils {
	public static IBaseResource readPlanDefinitionFromRepository(
			FhirVersionEnum theFhirVersion, Repository theRepository, IIdType theId) {
		switch (theFhirVersion) {
			case DSTU3:
				return theRepository.read(org.hl7.fhir.dstu3.model.PlanDefinition.class, theId);
			case R4:
				return theRepository.read(org.hl7.fhir.r4.model.PlanDefinition.class, theId);
			case R5:
				return theRepository.read(org.hl7.fhir.r5.model.PlanDefinition.class, theId);
			default:
				return null;
		}
	}
}
