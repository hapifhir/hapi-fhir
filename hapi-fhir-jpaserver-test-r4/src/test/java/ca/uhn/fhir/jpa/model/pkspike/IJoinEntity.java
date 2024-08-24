package ca.uhn.fhir.jpa.model.pkspike;

public interface IJoinEntity<P> {
	Long getPid();

	void setString(String theString);

	void setParent(P theRoot);

	String getString();

	void setPartitionId(Integer thePartitionId);

	Integer getPartitionId();

	Long getResId();
}
