package ca.uhn.fhir.jpa.model.entity;

import java.util.Objects;

public class IdAndPartitionIdValue {
	private Long myId;

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof IdAndPartitionIdValue)) return false;
		IdAndPartitionIdValue that = (IdAndPartitionIdValue) theO;
		return Objects.equals(myId, that.myId) && Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myId, myPartitionIdValue);
	}

	private Integer myPartitionIdValue;

	public IdAndPartitionIdValue() {
		// nothing
	}

	public IdAndPartitionIdValue(Long theId) {
		myId = theId;
	}
}
