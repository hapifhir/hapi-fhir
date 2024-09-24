package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public class IdAndPartitionId {
	private Long myId;
	private Integer myPartitionIdValue;

	public IdAndPartitionId(Long theId, Integer thePartitionId) {
		myId = theId;
		// FIXME: rename myPartitionIdValue to myPartitionId everywhere
		// FIXME: could we merge this into JpaPid?
		myPartitionIdValue = thePartitionId;
	}

	public IdAndPartitionId() {
		// nothing
	}

	public IdAndPartitionId(Long theId) {
		myId = theId;
	}

	public Long getId() {
		return myId;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setPartitionIdValue(Integer thePartitionIdValue) {
		myPartitionIdValue = thePartitionIdValue;
	}

	public Integer getPartitionIdValue() {
		return myPartitionIdValue;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof IdAndPartitionId)) return false;
		IdAndPartitionId that = (IdAndPartitionId) theO;
		return Objects.equals(myId, that.myId) && Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myId)
				.append("partitionId", myPartitionIdValue)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(myId, myPartitionIdValue);
	}
}
