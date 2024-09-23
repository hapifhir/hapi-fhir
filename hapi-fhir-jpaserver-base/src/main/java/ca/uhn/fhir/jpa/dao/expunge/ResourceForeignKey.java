/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.expunge;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResourceForeignKey {
	public final String myTable;
	public final String myResourceIdColumn;
	public final String myPartitionIdColumn;

	public ResourceForeignKey(String theTable, String thePartitionIdColumn, String theResourceIdColumn) {
		myTable = theTable;
		myPartitionIdColumn = thePartitionIdColumn;
		myResourceIdColumn = theResourceIdColumn;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		ResourceForeignKey that = (ResourceForeignKey) theO;

		return new EqualsBuilder()
				.append(myTable, that.myTable)
				.append(myResourceIdColumn, that.myResourceIdColumn)
				.append(myPartitionIdColumn, that.myPartitionIdColumn)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myTable).append(myPartitionIdColumn).append(myResourceIdColumn).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("table", myTable)
				.append("resourceIdColumn", myResourceIdColumn)
				.append("partitionIdColumn", myPartitionIdColumn)
				.toString();
	}
}
