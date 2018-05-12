package ca.uhn.fhir.jpa.util;

import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DerbyTenSevenHapiFhirDialect extends DerbyTenSevenDialect {

	private static final Logger ourLog = LoggerFactory.getLogger(DerbyTenSevenHapiFhirDialect.class);

	@Override
	public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
		return new TemplatedViolatedConstraintNameExtracter() {
			@Override
			protected String doExtractConstraintName(SQLException theSqlException) throws NumberFormatException {
				switch (theSqlException.getSQLState()) {
					case "23505":
						return this.extractUsingTemplate("unique or primary key constraint or unique index identified by '", "'", theSqlException.getMessage());
					default:
						return null;
				}
			}
		};
	}

}
