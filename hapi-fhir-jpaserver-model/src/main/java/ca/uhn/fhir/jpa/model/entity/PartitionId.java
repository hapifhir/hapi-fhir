package ca.uhn.fhir.jpa.model.entity;

import javax.annotation.Nullable;
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
	public PartitionId(@Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		setPartitionId(thePartitionId);
		setPartitionDate(thePartitionDate);
	}

	@Nullable
	public Integer getPartitionId() {
		return myPartitionId;
	}

	public PartitionId setPartitionId(@Nullable Integer thePartitionId) {
		myPartitionId = thePartitionId;
		return this;
	}

	@Nullable
	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	public PartitionId setPartitionDate(@Nullable LocalDate thePartitionDate) {
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
		return getPartitionIdStringOrNullString();
	}

	/**
	 * Returns the partition ID (numeric) as a string, or the string "null"
	 */
	public String getPartitionIdStringOrNullString() {
		return defaultIfNull(myPartitionId, "null").toString();
	}

	/**
	 * Create a string representation suitable for use as a cache key. Null aware.
	 */
	public static String stringifyForKey(PartitionId thePartitionId) {
		String retVal = "(null)";
		if (thePartitionId != null) {
			retVal = thePartitionId.getPartitionIdStringOrNullString();
		}
		return retVal;
	}
}
