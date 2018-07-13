package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import net.ttddyy.dsproxy.listener.ThreadQueryCountHolder;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.query.criteria.LiteralHandlingMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Configuration
@EnableTransactionManagement()
public class TestR4Config extends BaseJavaConfigR4 {

	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestR4Config.class);
	private static int ourMaxThreads;

	static {
		/*
		 * We use a randomized number of maximum threads in order to try
		 * and catch any potential deadlocks caused by database connection
		 * starvation
		 */
		ourMaxThreads = (int) (Math.random() * 6.0) + 1;
	}

	private Exception myLastStackTrace;

	@Bean()
	public DaoConfig daoConfig() {
		return new DaoConfig();
	}

	@Bean()
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource() {


			@Override
			public Connection getConnection() throws SQLException {
				ConnectionWrapper retVal;
				try {
					retVal = new ConnectionWrapper(super.getConnection());
				} catch (Exception e) {
					ourLog.error("Exceeded maximum wait for connection", e);
					logGetConnectionStackTrace();
//					if ("true".equals(System.getStringProperty("ci"))) {
					fail("Exceeded maximum wait for connection: " + e.toString());
//					}
//					System.exit(1);
					retVal = null;
				}

				try {
					throw new Exception();
				} catch (Exception e) {
					myLastStackTrace = e;
				}

				return retVal;
			}

			private void logGetConnectionStackTrace() {
				StringBuilder b = new StringBuilder();
				b.append("Last connection request stack trace:");
				for (StackTraceElement next : myLastStackTrace.getStackTrace()) {
					b.append("\n   ");
					b.append(next.getClassName());
					b.append(".");
					b.append(next.getMethodName());
					b.append("(");
					b.append(next.getFileName());
					b.append(":");
					b.append(next.getLineNumber());
					b.append(")");
				}
				ourLog.info(b.toString());
			}

		};
		retVal.setDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		retVal.setUrl("jdbc:derby:memory:myUnitTestDBR4;create=true");
		retVal.setMaxWaitMillis(10000);
		retVal.setUsername("");
		retVal.setPassword("");

		retVal.setMaxTotal(ourMaxThreads);

		DataSource dataSource = ProxyDataSourceBuilder
			.create(retVal)
			.logQueryBySlf4j(SLF4JLogLevel.INFO, "SQL")
			.logSlowQueryBySlf4j(10, TimeUnit.SECONDS)
			.countQuery(new ThreadQueryCountHolder())
			.build();

		return dataSource;
	}

	@Override
	@Bean()
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = super.entityManagerFactory();
		retVal.setPersistenceUnitName("PU_HapiFhirJpaR4");
		retVal.setDataSource(dataSource());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.jdbc.batch_size", "1");
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", "ca.uhn.fhir.jpa.util.DerbyTenSevenHapiFhirDialect");
		extraProperties.put("hibernate.search.model_mapping", ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.default.directory_provider", "ram");
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
		extraProperties.put("hibernate.search.autoregister_listeners", "true");
		extraProperties.put("hibernate.criteria.literal_handling_mode", LiteralHandlingMode.BIND);

		return extraProperties;
	}

	/**
	 * Bean which validates incoming requests
	 */
	@Bean
	@Lazy
	public RequestValidatingInterceptor requestValidatingInterceptor() {
		RequestValidatingInterceptor requestValidator = new RequestValidatingInterceptor();
		requestValidator.setFailOnSeverity(ResultSeverityEnum.ERROR);
		requestValidator.setAddResponseHeaderOnSeverity(null);
		requestValidator.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		requestValidator.addValidatorModule(instanceValidatorR4());

		return requestValidator;
	}

	@Bean()
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

	@Bean
	public UnregisterScheduledProcessor unregisterScheduledProcessor(Environment theEnv) {
		return new UnregisterScheduledProcessor(theEnv);
	}

	public static int getMaxThreads() {
		return ourMaxThreads;
	}

}
