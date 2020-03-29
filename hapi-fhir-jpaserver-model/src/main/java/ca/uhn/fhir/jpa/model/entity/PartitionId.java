package ca.uhn.fhir.jpa.model.entity;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Embeddable
public class PartitionId implements Cloneable {

	static final String PARTITION_ID = "PARTITION_ID";

	@Column(name = PARTITION_ID, nullable = true, insertable = true, updatable = false)
	private Integer myPartitionId;
	@Column(name = "PARTITION_DATE", nullable = true, insertable = true, updatable = false)
	private LocalDate myPartitionDate;

	/**
	 * Constructor
	 */
	public PartitionId() {
		super();
	}

	/**
	 * Constructor
	 */
	public PartitionId(int thePartitionId, LocalDate thePartitionDate) {
		setPartitionId(thePartitionId);
		setPartitionDate(thePartitionDate);
	}

	@Nonnull
	public Integer getPartitionId() {
		return myPartitionId;
	}

	public PartitionId setPartitionId(@Nonnull Integer thePartitionId) {
		myPartitionId = thePartitionId;
		return this;
	}

	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	public PartitionId setPartitionDate(LocalDate thePartitionDate) {
		myPartitionDate = thePartitionDate;
		return this;
	}

	@SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
	@Override
	protected PartitionId clone() {
		return new PartitionId()
			.setPartitionId(getPartitionId())
			.setPartitionDate(getPartitionDate());
	}

	@Override
	public String toString() {
		return defaultIfNull(myPartitionId, "null").toString();
	}
}
