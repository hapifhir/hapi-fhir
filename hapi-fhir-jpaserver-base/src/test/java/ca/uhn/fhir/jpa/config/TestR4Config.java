package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.svc.BatchJobSubmitterImpl;
import ca.uhn.fhir.jpa.binstore.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.dao.BaseJpaTest.buildHeapLuceneHibernateSearchProperties;
import static org.junit.jupiter.api.Assertions.fail;

@Configuration
@Import({TestJPAConfig.class, BatchJobsConfig.class})
@EnableTransactionManagement()
public class TestR4Config extends BaseJavaConfigR4 {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestR4Config.class);
	public static Integer ourMaxThreads;

	static {
		/*
		 * We use a randomized number of maximum threads in order to try
		 * and catch any potential deadlocks caused by database connection
		 * starvation
		 */
		if (ourMaxThreads == null) {
			ourMaxThreads = (int) (Math.random() * 6.0) + 3;

			if ("true".equals(System.getProperty("single_db_connection"))) {
				ourMaxThreads = 1;
			}
		}
	}


	private Exception myLastStackTrace;

	@Override
	@Bean
	public IBatchJobSubmitter batchJobSubmitter() {
		return new BatchJobSubmitterImpl();
	}

	@Bean
	public CircularQueueCaptureQueriesListener captureQueriesListener() {
		return new CircularQueueCaptureQueriesListener();
	}

	@Bean
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource() {

			@Override
			public Connection getConnection() {
				ConnectionWrapper retVal;
				try {
					retVal = new ConnectionWrapper(super.getConnection());
				} catch (Exception e) {
					ourLog.error("Exceeded maximum wait for connection", e);
					logGetConnectionStackTrace();
					fail("Exceeded maximum wait for connection: " + e.toString());
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

		retVal.setDriver(new org.h2.Driver());
		retVal.setUrl("jdbc:h2:mem:testdb_r4");
		retVal.setMaxWaitMillis(30000);
		retVal.setUsername("");
		retVal.setPassword("");
		retVal.setMaxTotal(ourMaxThreads);

		SLF4JLogLevel level = SLF4JLogLevel.INFO;
		DataSource dataSource = ProxyDataSourceBuilder
			.create(retVal)
//			.logQueryBySlf4j(level)
			.logSlowQueryBySlf4j(10, TimeUnit.SECONDS, level)
			.beforeQuery(new BlockLargeNumbersOfParamsListener())
			.beforeQuery(new MandatoryTransactionListener())
			.afterQuery(captureQueriesListener())
			.afterQuery(new CurrentThreadCaptureQueriesListener())
			.countQuery(singleQueryCountHolder())
			.afterMethod(captureQueriesListener())
			.build();

		return dataSource;
	}

	@Bean
	public SingleQueryCountHolder singleQueryCountHolder() {
		return new SingleQueryCountHolder();
	}


	@Override
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(ConfigurableListableBeanFactory theConfigurableListableBeanFactory) {
		LocalContainerEntityManagerFactoryBean retVal = new HapiFhirLocalContainerEntityManagerFactoryBean(theConfigurableListableBeanFactory);
		configureEntityManagerFactory(retVal, fhirContext());
		retVal.setJpaDialect(new HapiFhirHibernateJpaDialect(fhirContext().getLocalizer()));
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.model.entity", "ca.uhn.fhir.jpa.entity");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
		retVal.setPersistenceUnitName("PU_HapiFhirJpaR4");
		retVal.setDataSource(dataSource());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	@Bean
	public Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", H2Dialect.class.getName());

		boolean enableLucene = myEnv.getProperty(BaseJpaTest.CONFIG_ENABLE_LUCENE, Boolean.TYPE, BaseJpaTest.CONFIG_ENABLE_LUCENE_DEFAULT_VALUE);
		Map<String, String> hibernateSearchProperties = BaseJpaTest.buildHibernateSearchProperties(enableLucene);
		extraProperties.putAll(hibernateSearchProperties);

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

	@Bean
	public IBinaryStorageSvc binaryStorage() {
		return new MemoryBinaryStorageSvcImpl();
	}

	public static int getMaxThreads() {
		return ourMaxThreads;
	}

}
