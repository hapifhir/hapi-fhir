package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionResourceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface INpmPackageVersionResourceDao extends JpaRepository<NpmPackageVersionResourceEntity, Long>, IHapiFhirJpaRepository {

	@Query("SELECT e FROM NpmPackageVersionResourceEntity e WHERE e.myResourceType = :resourceType AND e.myFhirVersion = :fhirVersion AND e.myPackageVersion.myCurrentVersion = true")
	Slice<NpmPackageVersionResourceEntity> findCurrentVersionByResourceType(Pageable thePage, @Param("fhirVersion") FhirVersionEnum theFhirVersion, @Param("resourceType") String theResourceType);

	@Query("SELECT e FROM NpmPackageVersionResourceEntity e WHERE e.myCanonicalUrl = :url AND e.myFhirVersion = :fhirVersion AND e.myPackageVersion.myCurrentVersion = true")
	Slice<NpmPackageVersionResourceEntity> findCurrentVersionByCanonicalUrl(Pageable thePage, @Param("fhirVersion") FhirVersionEnum theFhirVersion, @Param("url") String theCanonicalUrl);

	@Query("SELECT e FROM NpmPackageVersionResourceEntity e WHERE e.myCanonicalUrl = :url AND e.myCanonicalVersion = :version AND e.myFhirVersion = :fhirVersion AND e.myPackageVersion.myCurrentVersion = true")
	Slice<NpmPackageVersionResourceEntity> findCurrentVersionByCanonicalUrlAndVersion(Pageable theOf, @Param("fhirVersion") FhirVersionEnum theFhirVersion, @Param("url") String theCanonicalUrl, @Param("version") String theCanonicalVersion);
}
