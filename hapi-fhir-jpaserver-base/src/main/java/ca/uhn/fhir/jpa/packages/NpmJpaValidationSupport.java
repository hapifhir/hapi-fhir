package ca.uhn.fhir.jpa.packages;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.support.CodeValidationResult;
import ca.uhn.fhir.context.support.support.LookupCodeResult;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;

public class NpmJpaValidationSupport implements IValidationSupport {

	@Autowired
	private FhirContext myFhirContext;

	// FIXME: iantorno
	// Autowire this into the test and then in before, it has a method called installPackage, go through the necessary
	// import
	// In any case where a value set is straightforward it will be func identical
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

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return false;
	}

	@Override
	public CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return null;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @NotNull IBaseResource theValueSet) {
		return null;
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode) {
		return null;
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return false;
	}

	//fixme Codesystem has content == complete or external, snomed has millions of codes so they are not included
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
}
