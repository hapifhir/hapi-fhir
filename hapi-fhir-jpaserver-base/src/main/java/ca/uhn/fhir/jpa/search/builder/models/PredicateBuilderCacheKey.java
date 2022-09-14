package ca.uhn.fhir.jpa.search.builder.models;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PredicateBuilderCacheKey {
	private final DbColumn myDbColumn;
	private final PredicateBuilderTypeEnum myType;
	private final String myParamName;
	private final int myHashCode;

	public PredicateBuilderCacheKey(DbColumn theDbColumn, PredicateBuilderTypeEnum theType, String theParamName) {
		myDbColumn = theDbColumn;
		myType = theType;
		myParamName = theParamName;
		myHashCode = new HashCodeBuilder().append(myDbColumn).append(myType).append(myParamName).toHashCode();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		PredicateBuilderCacheKey that = (PredicateBuilderCacheKey) theO;

		return new EqualsBuilder()
			.append(myDbColumn, that.myDbColumn)
			.append(myType, that.myType)
			.append(myParamName, that.myParamName)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return myHashCode;
	}
}
