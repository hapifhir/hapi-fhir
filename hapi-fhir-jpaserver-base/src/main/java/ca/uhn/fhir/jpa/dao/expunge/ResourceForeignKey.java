package ca.uhn.fhir.jpa.dao.expunge;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceForeignKey {
	public final String table;
	public final String key;

	public ResourceForeignKey(String theTable, String theKey) {
		table = theTable;
		key = theKey;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		ResourceForeignKey that = (ResourceForeignKey) theO;

		return new EqualsBuilder()
			.append(table, that.table)
			.append(key, that.key)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(table)
			.append(key)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("table", table)
			.append("key", key)
			.toString();
	}
}
