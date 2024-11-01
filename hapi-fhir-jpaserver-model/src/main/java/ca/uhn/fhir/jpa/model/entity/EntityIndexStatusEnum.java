package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nullable;

public enum EntityIndexStatusEnum {
	INDEXED_RDBMS_ONLY((short) 0),

	INDEXED_ALL((short) 1),

	INDEXING_FAILED((short) 2);

	private final short myColumnValue;

	EntityIndexStatusEnum(short theColumnValue) {
		myColumnValue = theColumnValue;
	}

	/**
	 * This is a replacement for {@link #ordinal()} since we want to force a specific
	 * ordinal (these values were historically not generated using an enum)
	 */
	public short getColumnValue() {
		return myColumnValue;
	}

	@Nullable
	public static EntityIndexStatusEnum fromColumnValue(@Nullable Short theColumnValue) {
		if (theColumnValue == null) {
			return null;
		}
		switch (theColumnValue) {
			case 0:
				return INDEXED_RDBMS_ONLY;
			case 1:
				return INDEXED_ALL;
			case 2:
				return INDEXING_FAILED;
			default:
				throw new IllegalArgumentException(Msg.code(2565) + "Invalid index status: " + theColumnValue);
		}
	}

	@Nullable
	public static Short toColumnValue(@Nullable EntityIndexStatusEnum theIndexStatus) {
		return theIndexStatus != null ? theIndexStatus.getColumnValue() : null;
	}
}
