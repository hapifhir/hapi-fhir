package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

public interface IPartition {
	Integer getId();

	IPartition setId(Integer theId);

	String getName();

	IPartition setName(String theName);

	String getDescription();

	void setDescription(String theDescription);

	RequestPartitionId toRequestPartitionId();
}
