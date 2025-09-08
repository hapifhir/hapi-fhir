/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.model.api.PagingIterator;
import ca.uhn.fhir.rest.annotation.Transaction;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NpmJpaValidationSupport implements IValidationSupport {
	private static final Logger ourLog = LoggerFactory.getLogger(NpmJpaValidationSupport.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiPackageCacheManager myHapiPackageCacheManager; // this is JpaPackageCache

	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;

	@Autowired
	private PackageResourceParsingSvc myPackageResourceParsingSvc;

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
			Class<? extends IBaseResource> type =
					myFhirContext.getResourceDefinition(theResourceType).getImplementingClass();
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

	@Override
	@Transaction
	public <T extends IBaseResource> List<T> fetchAllSearchParameters() {
		/*
		 * default findall is probably fine
		 * there's probably not a lot and even if there are
		 * we don't reorder them
		 */
		PagingIterator<NpmPackageVersionEntity> iterator =
				new PagingIterator<>(1000, (thePageIndex, theBatchSize, theConsumer) -> {
					myPackageVersionDao
						.findAll(Pageable.ofSize(theBatchSize).withPage(thePageIndex).getSortOr(Sort.by(Sort.Direction.ASC, "myPackageId", "myVersionId")))
						.forEach(theConsumer);
				});

		// do we want to cache this?
		List<T> sps = new ArrayList<>();
		while (iterator.hasNext()) {
			NpmPackageVersionEntity entity = iterator.next();
			getAllSP(entity, sps);
		}
		return sps;
	}

	private <T extends IBaseResource> void getAllSP(NpmPackageVersionEntity theNpmPackageEntity, List<T> theSps) {
		if (theNpmPackageEntity.getFhirVersion() != myFhirContext.getVersion().getVersion()) {
			// not the same fhir version as this class is dependent on,
			// so we cannot create these objects
			ourLog.info(
					"Encountered an NPM package with an incompatible fhir version: {} when expecting {}",
					theNpmPackageEntity.getFhirVersion().getFhirVersionString(),
					myFhirContext.getVersion().getVersion().getFhirVersionString());
			return;
		}

		NpmPackage pkg;
		try {
			pkg = myHapiPackageCacheManager.loadPackage(
					theNpmPackageEntity.getPackageId(), theNpmPackageEntity.getVersionId());
		} catch (IOException ex) {
			// TODO - better handling
			throw new RuntimeException(ex);
		}

		List<IBaseResource> pkgSps = myPackageResourceParsingSvc.parseResourcesOfType("SearchParameter", pkg);
		for (IBaseResource sp : pkgSps) {
			PackageUtils.addPackageMetadata(sp, pkg);
			theSps.add((T) sp);
		}
	}
}
