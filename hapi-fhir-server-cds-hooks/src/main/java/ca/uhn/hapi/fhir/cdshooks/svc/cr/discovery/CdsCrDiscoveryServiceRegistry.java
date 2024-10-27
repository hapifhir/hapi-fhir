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
package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.fhir.context.FhirVersionEnum;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CdsCrDiscoveryServiceRegistry implements ICdsCrDiscoveryServiceRegistry {
	private final Map<FhirVersionEnum, Class<? extends ICrDiscoveryService>> myCrDiscoveryServices;

	public CdsCrDiscoveryServiceRegistry() {
		myCrDiscoveryServices = new HashMap<>();
		myCrDiscoveryServices.put(FhirVersionEnum.DSTU3, CrDiscoveryServiceDstu3.class);
		myCrDiscoveryServices.put(FhirVersionEnum.R4, CrDiscoveryServiceR4.class);
		myCrDiscoveryServices.put(FhirVersionEnum.R5, CrDiscoveryServiceR5.class);
	}

	public void register(
			@Nonnull FhirVersionEnum theFhirVersion,
			@Nonnull Class<? extends ICrDiscoveryService> theCrDiscoveryService) {
		myCrDiscoveryServices.put(theFhirVersion, theCrDiscoveryService);
	}

	public void unregister(@Nonnull FhirVersionEnum theFhirVersion) {
		myCrDiscoveryServices.remove(theFhirVersion);
	}

	@Override
	public Optional<Class<? extends ICrDiscoveryService>> find(@Nonnull FhirVersionEnum theFhirVersion) {
		return Optional.ofNullable(myCrDiscoveryServices.get(theFhirVersion));
	}
}
