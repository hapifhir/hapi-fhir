/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ITagDefinitionDao extends JpaRepository<TagDefinition, Long>, IHapiFhirJpaRepository {
	@Query("SELECT t FROM TagDefinition t WHERE " + "t.myTagType = :tagType AND "
			+ "( :scheme IS NULL OR :scheme = '' OR t.mySystem = :scheme ) AND "
			+ "t.myCode = :term AND "
			+ "( :version IS NULL OR :version = '' OR t.myVersion = :version ) AND "
			+ "( :userSelected IS NULL OR t.myUserSelected = :userSelected )")
	List<TagDefinition> findByTagTypeAndSchemeAndTermAndVersionAndUserSelected(
			@Param("tagType") TagTypeEnum tagType,
			@Param("scheme") String scheme,
			@Param("term") String term,
			@Param("version") String version,
			@Param("userSelected") Boolean userSelected,
			Pageable pageable);

	/**
	 * Fetches all tag definitions of the given type whose code is in the supplied collection. Used to
	 * batch-resolve the tag ids for a {@code _tag}/{@code _security}/{@code _profile} search in a single
	 * lookup; the system is matched by the caller so this deliberately filters on type + code only.
	 */
	@Query("SELECT t FROM TagDefinition t WHERE t.myTagType = :tagType AND t.myCode IN :codes")
	List<TagDefinition> findByTagTypeAndCodes(
			@Param("tagType") TagTypeEnum tagType, @Param("codes") Collection<String> codes);
}
