/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.tinder.ddl.test.embeddedpk;

import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import ca.uhn.hapi.fhir.sql.hibernatesvc.ConditionalIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

/**
 * JPA implementation of IResourcePersistentId.  JPA uses a Long as the primary key.  This class should be used in any
 * context where the pid is known to be a Long.
 */
@Embeddable
public class JpaPid {

	@SequenceGenerator(name = "SEQ_RESOURCE_ID", sequenceName = "SEQ_RESOURCE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_ID")
	@Column(name = "RES_ID")
	@GenericField(projectable = Projectable.YES)
	private Long myId;

	@ConditionalIdProperty
	@Column(name = "PARTITION_ID")
	private Integer myPartitionIdValue;

}
