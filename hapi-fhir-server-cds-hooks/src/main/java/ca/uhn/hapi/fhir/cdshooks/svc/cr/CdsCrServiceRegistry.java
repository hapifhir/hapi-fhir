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
package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirVersionEnum;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CdsCrServiceRegistry implements ICdsCrServiceRegistry {
	private final Map<FhirVersionEnum, Class<? extends ICdsCrService>> myCdsCrServices;

	public CdsCrServiceRegistry() {
		myCdsCrServices = new HashMap<>();
		myCdsCrServices.put(FhirVersionEnum.DSTU3, CdsCrServiceDstu3.class);
		myCdsCrServices.put(FhirVersionEnum.R4, CdsCrServiceR4.class);
		myCdsCrServices.put(FhirVersionEnum.R5, CdsCrServiceR5.class);
	}

	public void register(
			@Nonnull FhirVersionEnum theFhirVersion, @Nonnull Class<? extends ICdsCrService> theCdsCrService) {
		myCdsCrServices.put(theFhirVersion, theCdsCrService);
	}

	public void unregister(@Nonnull FhirVersionEnum theFhirVersion) {
		myCdsCrServices.remove(theFhirVersion);
	}

	public Optional<Class<? extends ICdsCrService>> find(@Nonnull FhirVersionEnum theFhirVersion) {
		return Optional.ofNullable(myCdsCrServices.get(theFhirVersion));
	}
}
