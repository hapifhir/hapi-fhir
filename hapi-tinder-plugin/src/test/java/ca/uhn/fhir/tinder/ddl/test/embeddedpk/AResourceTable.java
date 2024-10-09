/*
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

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = AResourceTable.HFJ_RESOURCE,
		uniqueConstraints = {
			@UniqueConstraint(
					name = AResourceTable.IDX_RES_TYPE_FHIR_ID,
					columnNames = {"RES_TYPE", "FHIR_ID"})
		},
		indexes = {
			// Do not reuse previously used index name: IDX_INDEXSTATUS, IDX_RES_TYPE
			@Index(name = "IDX_RES_DATE", columnList = "RES_UPDATED"),
			@Index(name = "IDX_RES_FHIR_ID", columnList = "FHIR_ID"),
			@Index(
					name = "IDX_RES_TYPE_DEL_UPDATED",
					columnList = "RES_TYPE,RES_DELETED_AT,RES_UPDATED,PARTITION_ID,RES_ID"),
			@Index(name = "IDX_RES_RESID_UPDATED", columnList = "RES_ID, RES_UPDATED, PARTITION_ID")
		})
@NamedEntityGraph(name = "Resource.noJoins")
public class AResourceTable {
	public static final int RESTYPE_LEN = 40;
	public static final String HFJ_RESOURCE = "HFJ_RESOURCE";
	public static final String IDX_RES_TYPE_FHIR_ID = "IDX_RES_TYPE_FHIR_ID";

	@EmbeddedId
	private JpaPid myPid;

}
