package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.model.dialect.IHapiFhirDialect;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DialectSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(DialectSvc.class);

	private static boolean ourForceMsSqlMode = false;

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private final Optional<DriverTypeEnum> myDriverType;

	public DialectSvc(HibernatePropertiesProvider theHibernatePropertiesProvider) {
		Dialect dialect = theHibernatePropertiesProvider.getDialect();
		if (!(dialect instanceof IHapiFhirDialect)) {
			ourLog.warn("Dialect is not a HAPI FHIR dialect: {}", dialect);
			myDriverType = Optional.empty();
		} else {
			myDriverType = Optional.of(((IHapiFhirDialect) dialect).getDriverType());
		}
	}

	/**
	 * This will be empty if a dialect is not a HAPI FHIR dialect. This shouldn't generally happen but
	 * is possible.
	 */
	@Nonnull
	public Optional<DriverTypeEnum> getDriverType() {
		return myDriverType;
	}

	public boolean isMssql() {
		if (ourForceMsSqlMode) {
			return true;
		}
		return getDriverType().orElse(null) == DriverTypeEnum.MSSQL_2012;
	}

	/**
	 * For unit testing only
	 */
	@VisibleForTesting
	public static void setForceMsSqlMode(boolean theForceMsSqlMode) {
		ourLog.warn("Forcing MS SQL mode: {}", theForceMsSqlMode);
		ourForceMsSqlMode = theForceMsSqlMode;
	}
}
