package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

public interface INpmPackageVersionDao extends JpaRepository<NpmPackageVersionEntity, Long>, IHapiFhirJpaRepository {

	@Query("SELECT p FROM NpmPackageVersionEntity p WHERE p.myPackageId = :id")
	Collection<NpmPackageVersionEntity> findByPackageId(@Param("id") String thePackageId);

	@Query("SELECT p FROM NpmPackageVersionEntity p WHERE p.myPackageId = :id AND p.myVersionId = :version")
	Optional<NpmPackageVersionEntity> findByPackageIdAndVersion(@Param("id") String thePackageId, @Param("version") String thePackageVersion);

	/**
	 * Uses a "like" expression on the version ID
	 */
	@Query("SELECT p.myVersionId FROM NpmPackageVersionEntity p WHERE p.myPackageId = :id AND p.myVersionId like :version")
	List<String> findVersionIdsByPackageIdAndLikeVersion(@Param("id") String theId, @Param("version") String thePartialVersionString);
}
