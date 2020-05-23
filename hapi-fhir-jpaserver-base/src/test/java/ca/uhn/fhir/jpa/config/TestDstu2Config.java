package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import net.ttddyy.dsproxy.listener.ThreadQueryCountHolder;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.H2Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

@Configuration
@Import(TestJPAConfig.class)
@EnableTransactionManagement()
public class TestDstu2Config extends BaseJavaConfigDstu2 {
	private static final Logger ourLog = LoggerFactory.getLogger(TestDstu2Config.class);
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
	private String myLastStackTraceThreadName;

	@Bean
	public CircularQueueCaptureQueriesListener captureQueriesListener() {
		return new CircularQueueCaptureQueriesListener();
	}

	@Bean
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
					myLastStackTraceThreadName = Thread.currentThread().getName();
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
				ourLog.info("Last connection thread: {}", myLastStackTraceThreadName);
			}

		};
		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:testdb_dstu2");
		retVal.setMaxWaitMillis(10000);
		retVal.setUsername("");
		retVal.setPassword("");

		retVal.setMaxTotal(ourMaxThreads);

		DataSource dataSource = ProxyDataSourceBuilder
			.create(retVal)
//			.logQueryBySlf4j(SLF4JLogLevel.INFO, "SQL")
			.logSlowQueryBySlf4j(10, TimeUnit.SECONDS)
			.afterQuery(captureQueriesListener())
			.afterQuery(new CurrentThreadCaptureQueriesListener())
			.countQuery(new ThreadQueryCountHolder())
			.build();

		return dataSource;
	}

	@Override
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = super.entityManagerFactory();
		retVal.setPersistenceUnitName("PU_HapiFhirJpaDstu2");
		retVal.setDataSource(dataSource());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	@Bean(name = "maxDatabaseThreadsForTest")
	public Integer getMaxThread() {
		return ourMaxThreads;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "true");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", H2Dialect.class.getName());
		extraProperties.put("hibernate.search.model_mapping", LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.default.directory_provider", "local-heap");
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
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
		requestValidator.addValidatorModule(instanceValidator());

		return requestValidator;
	}


}
