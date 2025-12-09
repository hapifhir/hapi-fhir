package ca.uhn.fhir.jpa.model.pkspike;

import java.util.Collection;

public interface IRootEntity<J> {
	Long getResId();

	void setPartitionId(Integer thePartitionId);

	Integer getPartitionId();

	String getString();

	void setString(String theString);

	Collection<J> getJoins();
}
