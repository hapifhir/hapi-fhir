package ca.uhn.fhir.jpa.logging;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.service.spi.ServiceContributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilteringSqlLoggerImplContributor implements ServiceContributor {
	private static final Logger logger = LoggerFactory.getLogger(FilteringSqlLoggerImplContributor.class);

	@Override
	public void contribute(StandardServiceRegistryBuilder serviceRegistryBuilder) {
		logger.info("Adding service: SqlStatementFilteringLogger");

		serviceRegistryBuilder.addService(
				SqlStatementLogger.class, new SqlStatementFilteringLogger(SqlLoggerFilteringUtil.getInstance()));
	}
}
