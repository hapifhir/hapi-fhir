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

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Multi-column primary Key for {@link ResourceSearchUrlEntity}
 */
@Embeddable
public class ResourceSearchUrlEntityPK implements Serializable {
	public static final String RES_SEARCH_URL_COLUMN_NAME = "RES_SEARCH_URL";
	public static final String PARTITION_ID_COLUMN_NAME = "PARTITION_ID";

	public static final int RES_SEARCH_URL_LENGTH = 768;

	private static final long serialVersionUID = 1L;

	@Column(name = RES_SEARCH_URL_COLUMN_NAME, length = RES_SEARCH_URL_LENGTH, nullable = false)
	// Weird field name isto ensure that this the first key in the index
	private String my_A_SearchUrl;

	@Column(name = PARTITION_ID_COLUMN_NAME, nullable = false, insertable = false, updatable = false)
	// Weird field name isto ensure that this the second key in the index
	private Integer my_B_PartitionId;

	public ResourceSearchUrlEntityPK() {}

}
