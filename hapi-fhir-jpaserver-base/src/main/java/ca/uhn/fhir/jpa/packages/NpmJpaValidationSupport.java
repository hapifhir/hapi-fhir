package ca.uhn.fhir.jpa.packages;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.List;

public class NpmJpaValidationSupport implements IValidationSupport {

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiPackageCacheManager myHapiPackageCacheManager;

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public IBaseResource fetchValueSet(String theUri) {
		return fetchResource("ValueSet", theUri);
	}

	@Override
	public IBaseResource fetchCodeSystem(String theUri) {
		return fetchResource("CodeSystem", theUri);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUri) {
		return fetchResource("StructureDefinition", theUri);
	}

	@Nullable
	public IBaseResource fetchResource(String theResourceType, String theUri) {
		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();
		IBaseResource asset = myHapiPackageCacheManager.loadPackageAssetByUrl(fhirVersion, theUri);
		if (asset != null) {
			Class<? extends IBaseResource> type = myFhirContext.getResourceDefinition(theResourceType).getImplementingClass();
			if (type.isAssignableFrom(asset.getClass())) {
				return asset;
			}
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();
		return (List<T>) myHapiPackageCacheManager.loadPackageAssetsByType(fhirVersion, "StructureDefinition");
	}

}
