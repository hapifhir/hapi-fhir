/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.test.config;

import ca.uhn.fhir.batch2.jobs.config.Batch2JobsConfig;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch2.JpaBatch2Config;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.config.HapiJpaConfig;
import ca.uhn.fhir.jpa.config.r4b.JpaR4BConfig;
import ca.uhn.fhir.jpa.config.util.HapiEntityManagerFactoryUtil;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicConfig;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.CurrentThreadCaptureQueriesListener;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.system.HapiTestSystemProperties;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.sql.Connection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.test.config.TestR5Config.SELECT_QUERY_INCLUSION_CRITERIA_EXCLUDING_SEQUENCE_QUERIES;
import static org.junit.jupiter.api.Assertions.fail;

@Configuration
@Import({
	JpaR4BConfig.class,
	HapiJpaConfig.class,
	TestJPAConfig.class,
	SubscriptionTopicConfig.class,
	TestHSearchAddInConfig.DefaultLuceneHeap.class,
	JpaBatch2Config.class,
	Batch2JobsConfig.class
})
public class TestR4BConfig {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestR4BConfig.class);
	public static Integer ourMaxThreads;

	static {
		/*
		 * Set a reasonable number of maximum connections so that anything that
		 * runs away demanding large numbers of connections causes a failure.
		 */
		if (ourMaxThreads == null) {
			ourMaxThreads = 8;

			if (HapiTestSystemProperties.isSingleDbConnectionEnabled()) {
				ourMaxThreads = 1;
			}
			if (HapiTestSystemProperties.isUnlimitedDbConnectionsEnabled()) {
				ourMaxThreads = 100;
			}
		}
	}

	private final Deque<Exception> myLastStackTrace = new LinkedList<>();
	@Autowired
	TestHSearchAddInConfig.IHSearchConfigurer hibernateSearchConfigurer;
	private boolean myHaveDumpedThreads;

	@Bean
	public CircularQueueCaptureQueriesListener captureQueriesListener() {
		return new CircularQueueCaptureQueriesListener()
				.setSelectQueryInclusionCriteria(SELECT_QUERY_INCLUSION_CRITERIA_EXCLUDING_SEQUENCE_QUERIES);
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
					ourLog.error("Exceeded maximum wait for connection (" + ourMaxThreads + " max)", e);
					logGetConnectionStackTrace();
					fail("Exceeded maximum wait for connection (" + ourMaxThreads + " max): " + e);
					retVal = null;
				}

				try {
					throw new Exception();
				} catch (Exception e) {
					synchronized (myLastStackTrace) {
						myLastStackTrace.add(e);
						while (myLastStackTrace.size() > ourMaxThreads) {
							myLastStackTrace.removeFirst();
						}
					}
				}

				return retVal;
			}

			private void logGetConnectionStackTrace() {
				StringBuilder b = new StringBuilder();
				int i = 0;
				synchronized (myLastStackTrace) {
					for (Iterator<Exception> iter = myLastStackTrace.descendingIterator(); iter.hasNext(); ) {
						Exception nextStack = iter.next();
						b.append("\n\nPrevious request stack trace ");
						b.append(i++);
						b.append(":");
						for (StackTraceElement next : nextStack.getStackTrace()) {
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
					}
				}
				ourLog.info(b.toString());

				if (!myHaveDumpedThreads) {
					ourLog.info("Thread dump:" + crunchifyGenerateThreadDump());
					myHaveDumpedThreads = true;
				}
			}

		};

		setConnectionProperties(retVal);

		SLF4JLogLevel level = SLF4JLogLevel.INFO;
		DataSource dataSource = ProxyDataSourceBuilder
			.create(retVal)
//			.logQueryBySlf4j(level)
			.logSlowQueryBySlf4j(10, TimeUnit.SECONDS, level)
			.beforeQuery(new BlockLargeNumbersOfParamsListener())
			.beforeQuery(getMandatoryTransactionListener())
			.afterQuery(captureQueriesListener())
			.afterQuery(new CurrentThreadCaptureQueriesListener())
			.countQuery(singleQueryCountHolder())
			.afterMethod(captureQueriesListener())
			.build();

		return dataSource;
	}


	public void setConnectionProperties(BasicDataSource theDataSource) {
		theDataSource.setDriver(new org.h2.Driver());
		theDataSource.setUrl("jdbc:h2:mem:testdb_r4b");
		theDataSource.setMaxWaitMillis(30000);
		theDataSource.setUsername("");
		theDataSource.setPassword("");
		theDataSource.setMaxTotal(ourMaxThreads);
	}


	@Bean
	public SingleQueryCountHolder singleQueryCountHolder() {
		return new SingleQueryCountHolder();
	}

	@Bean
	public ProxyDataSourceBuilder.SingleQueryExecution getMandatoryTransactionListener() {
		return new MandatoryTransactionListener();
	}


	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(ConfigurableListableBeanFactory theConfigurableListableBeanFactory, FhirContext theFhirContext, JpaStorageSettings theStorageSettings) {
		LocalContainerEntityManagerFactoryBean retVal = HapiEntityManagerFactoryUtil.newEntityManagerFactory(theConfigurableListableBeanFactory, theFhirContext, theStorageSettings);
		retVal.setPersistenceUnitName("PU_HapiFhirJpaR4B");
		retVal.setDataSource(dataSource());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.format_sql", "false");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.dialect", getHibernateDialect());

		hibernateSearchConfigurer.apply(extraProperties);

		ourLog.info("jpaProperties: {}", extraProperties);

		return extraProperties;
	}

	public String getHibernateDialect() {
		return HapiFhirH2Dialect.class.getName();
	}

	/**
	 * Bean which validates incoming requests
	 */
	@Bean
	@Lazy
	public RequestValidatingInterceptor requestValidatingInterceptor(FhirInstanceValidator theFhirInstanceValidator) {
		RequestValidatingInterceptor requestValidator = new RequestValidatingInterceptor();
		requestValidator.setFailOnSeverity(ResultSeverityEnum.ERROR);
		requestValidator.setAddResponseHeaderOnSeverity(null);
		requestValidator.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		requestValidator.addValidatorModule(theFhirInstanceValidator);

		return requestValidator;
	}

	@Bean
	public IBinaryStorageSvc binaryStorage() {
		return new MemoryBinaryStorageSvcImpl();
	}

	public static String crunchifyGenerateThreadDump() {
		final StringBuilder dump = new StringBuilder();
		final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);
		for (ThreadInfo threadInfo : threadInfos) {
			dump.append('"');
			dump.append(threadInfo.getThreadName());
			dump.append("\" ");
			final Thread.State state = threadInfo.getThreadState();
			dump.append("\n   java.lang.Thread.State: ");
			dump.append(state);
			final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
			for (final StackTraceElement stackTraceElement : stackTraceElements) {
				dump.append("\n        at ");
				dump.append(stackTraceElement);
			}
			dump.append("\n\n");
		}
		return dump.toString();
	}

	public static int getMaxThreads() {
		return ourMaxThreads;
	}

}
