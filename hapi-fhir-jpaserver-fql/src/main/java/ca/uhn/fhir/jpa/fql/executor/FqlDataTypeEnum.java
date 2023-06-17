package ca.uhn.fhir.jpa.fql.executor;

import java.sql.Types;

public enum FqlDataTypeEnum {
	// FIXME: add tests for all of these
	STRING(Types.VARCHAR),
	INTEGER(Types.INTEGER),
	BOOLEAN(Types.BOOLEAN),
	DATE(Types.DATE),
	TIMESTAMP(Types.TIMESTAMP_WITH_TIMEZONE),
	LONGINT(Types.BIGINT),
	TIME(Types.TIME),
	DECIMAL(Types.DECIMAL);

	private final int mySqlType;

	FqlDataTypeEnum(int theSqlType) {
		mySqlType = theSqlType;
	}

	public int getSqlType() {
		return mySqlType;
	}
}
