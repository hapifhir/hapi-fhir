package ca.uhn.fhir.jpa.dao.predicate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SearchBuilderJoinKey {
	private final SearchBuilderJoinEnum myJoinType;
	private final String myParamName;

	public SearchBuilderJoinKey(String theParamName, SearchBuilderJoinEnum theJoinType) {
		super();
		myParamName = theParamName;
		myJoinType = theJoinType;
	}

	@Override
	public boolean equals(Object theObj) {
		if (!(theObj instanceof SearchBuilderJoinKey)) {
			return false;
		}
		SearchBuilderJoinKey obj = (SearchBuilderJoinKey) theObj;
		return new EqualsBuilder()
			.append(myParamName, obj.myParamName)
			.append(myJoinType, obj.myJoinType)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(myParamName)
			.append(myJoinType)
			.toHashCode();
	}
}
