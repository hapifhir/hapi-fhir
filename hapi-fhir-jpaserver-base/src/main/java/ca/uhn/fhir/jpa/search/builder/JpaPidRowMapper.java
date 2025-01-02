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
package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JpaPidRowMapper implements RowMapper<JpaPid> {

	private final boolean mySelectPartitionId;

	public JpaPidRowMapper(boolean theSelectPartitionId) {
		mySelectPartitionId = theSelectPartitionId;
	}

	@Override
	public JpaPid mapRow(ResultSet theResultSet, int theRowNum) throws SQLException {
		if (mySelectPartitionId) {
			Integer partitionId = theResultSet.getObject(1, Integer.class);
			Long resourceId = theResultSet.getLong(2);
			return JpaPid.fromId(resourceId, partitionId);
		} else {
			Long resourceId = theResultSet.getLong(1);
			return JpaPid.fromId(resourceId);
		}
	}
}
