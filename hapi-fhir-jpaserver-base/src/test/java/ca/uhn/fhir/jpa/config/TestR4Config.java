package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.binstore.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.H2Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

@Configuration
@Import(TestJPAConfig.class)
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
			ourMaxThreads = (int) (Math.random() * 6.0) + 1;
		}
	}

	@Autowired
	private Environment myEnvironment;

	private Exception myLastStackTrace;

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
		retVal.setMaxWaitMillis(10000);
		retVal.setUsername("");
		retVal.setPassword("");
		retVal.setMaxTotal(ourMaxThreads);

		SLF4JLogLevel level = SLF4JLogLevel.INFO;
		DataSource dataSource = ProxyDataSourceBuilder
			.create(retVal)
//			.logQueryBySlf4j(level, "SQL")
			.logSlowQueryBySlf4j(10, TimeUnit.SECONDS)
//			.countQuery(new ThreadQueryCountHolder())
			.beforeQuery(new BlockLargeNumbersOfParamsListener())
			.afterQuery(captureQueriesListener())
			.afterQuery(new CurrentThreadCaptureQueriesListener())
			.countQuery(singleQueryCountHolder())
			.build();

		return dataSource;
	}

	@Bean
	public SingleQueryCountHolder singleQueryCountHolder() {
		return new SingleQueryCountHolder();
	}

	@Override
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = super.entityManagerFactory();
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
		extraProperties.put("hibernate.search.model_mapping", ca.uhn.fhir.jpa.search.LuceneSearchMappingFactory.class.getName());
		extraProperties.put("hibernate.search.default.directory_provider", "local-heap");
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
		extraProperties.put("hibernate.search.autoregister_listeners", "true");

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
