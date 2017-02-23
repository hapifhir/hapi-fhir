/*
 *  Copyright 2016 Cognitive Medical Systems, Inc (http://www.cognitivemedicine.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ca.uhn.fhir.jpa.demo;

import ca.uhn.fhir.jpa.config.BaseJavaConfigDstu3;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.interceptor.RestHookSubscriptionDstu3Interceptor;
import ca.uhn.fhir.jpa.interceptor.WebSocketSubscriptionDstu3Interceptor;
import ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketReturnResourceHandlerDstu3;
import ca.uhn.fhir.jpa.util.SpringObjectCaster;
import ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptorDstu3;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * This class isn't used by default by the example, but 
 * you can use it as a config if you want to support DSTU3
 * instead of DSTU2 in your server as well as rest-hook subscriptions,
 * event driven web-socket subscriptions, and a mysql database.
 * 
 * See https://github.com/jamesagnew/hapi-fhir/issues/278
 */
@Configuration
@EnableWebSocket()
@EnableTransactionManagement()
public class FhirServerConfigDstu3WSocket extends BaseJavaConfigDstu3 implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry theRegistry) {
		theRegistry.addHandler(subscriptionWebSocketHandler(), "/websocket/dstu3");
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public WebSocketHandler subscriptionWebSocketHandler() {
		PerConnectionWebSocketHandler retVal = new PerConnectionWebSocketHandler(SubscriptionWebsocketReturnResourceHandlerDstu3.class);
		return retVal;
	}

	@Bean(destroyMethod="destroy")
	public TaskScheduler websocketTaskScheduler() {
		final ThreadPoolTaskScheduler retVal = new ThreadPoolTaskScheduler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void afterPropertiesSet() {
				super.afterPropertiesSet();
				getScheduledThreadPoolExecutor().setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
//				getScheduledThreadPoolExecutor().setRemoveOnCancelPolicy(true);
				getScheduledThreadPoolExecutor().setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
			}
		};
		retVal.setThreadNamePrefix("ws-dstu3-");
		retVal.setPoolSize(5);

		return retVal;
	}

	@Bean
	@Lazy
	public IServerInterceptor webSocketSubscriptionDstu3Interceptor(){
		return new WebSocketSubscriptionDstu3Interceptor();
	}

	@Bean
	@Lazy
	public IServerInterceptor restHookSubscriptionDstu3Interceptor(){
		return new RestHookSubscriptionDstu3Interceptor();
	}

	/**
	 * Configure FHIR properties around the the JPA server via this bean
	 */
	@Bean()
	public DaoConfig daoConfig() {
		DaoConfig retVal = new DaoConfig();
		retVal.setSubscriptionEnabled(true);
		retVal.setSubscriptionPollDelay(-1000);
		retVal.setSchedulingDisabled(true);
		retVal.setSubscriptionPurgeInactiveAfterMillis(DateUtils.MILLIS_PER_HOUR);
		retVal.setAllowMultipleDelete(true);
		retVal.setAllowExternalReferences(true);
		return retVal;
	}

	/**
	 * Loads the rest-hook and websocket interceptors after the DaoConfig bean has been
	 * initialized to avoid cyclical dependency errors
	 * @param daoConfig
	 * @return
	 */
	@Bean(name = "subscriptionInterceptors")
	@DependsOn("daoConfig")
	public List<IServerInterceptor> afterDaoConfig(DaoConfig daoConfig){
		IServerInterceptor webSocketInterceptor = webSocketSubscriptionDstu3Interceptor();
		IServerInterceptor restHookInterceptor = restHookSubscriptionDstu3Interceptor();

		try {
			RestHookSubscriptionDstu3Interceptor restHook = SpringObjectCaster.getTargetObject(restHookInterceptor, RestHookSubscriptionDstu3Interceptor.class);
			restHook.initSubscriptions();
		}catch(PersistenceException e){
			throw new RuntimeException("Persistence error in setting up resthook subscriptions:" + e.getMessage());
		}catch(Exception e){
			throw new RuntimeException("Unable to cast from proxy");
		}

		daoConfig.getInterceptors().add(restHookInterceptor);
		daoConfig.getInterceptors().add(webSocketInterceptor);

		return daoConfig.getInterceptors();
	}

	/**
	 * The following bean configures the database connection. The 'url' property value of "jdbc:derby:directory:jpaserver_derby_files;create=true" indicates that the server should save resources in a
	 * directory called "jpaserver_derby_files".
	 * 
	 * A URL to a remote database could also be placed here, along with login credentials and other properties supported by BasicDataSource.
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource retVal = new BasicDataSource();
		retVal.setDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		retVal.setUrl("jdbc:derby:directory:target/jpaserver_derby_files;create=true");
		retVal.setUsername("");
		retVal.setPassword("");
		return retVal;
	}

	@Bean()
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean retVal = new LocalContainerEntityManagerFactoryBean();
		retVal.setPersistenceUnitName("HAPI_PU");
		retVal.setDataSource(dataSource());
		retVal.setPackagesToScan("ca.uhn.fhir.jpa.entity");
		retVal.setPersistenceProvider(new HibernatePersistenceProvider());
		retVal.setJpaProperties(jpaProperties());
		return retVal;
	}

	private Properties jpaProperties() {
		Properties extraProperties = new Properties();
		extraProperties.put("hibernate.dialect", org.hibernate.dialect.DerbyTenSevenDialect.class.getName());
		extraProperties.put("hibernate.format_sql", "true");
		extraProperties.put("hibernate.show_sql", "false");
		extraProperties.put("hibernate.hbm2ddl.auto", "update");
		extraProperties.put("hibernate.jdbc.batch_size", "20");
		extraProperties.put("hibernate.cache.use_query_cache", "false");
		extraProperties.put("hibernate.cache.use_second_level_cache", "false");
		extraProperties.put("hibernate.cache.use_structured_entries", "false");
		extraProperties.put("hibernate.cache.use_minimal_puts", "false");
		extraProperties.put("hibernate.search.default.directory_provider", "filesystem");
		extraProperties.put("hibernate.search.default.indexBase", "target/lucenefiles");
		extraProperties.put("hibernate.search.lucene_version", "LUCENE_CURRENT");
		return extraProperties;
	}

	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 */
	public IServerInterceptor loggingInterceptor() {
		LoggingInterceptor retVal = new LoggingInterceptor();
		retVal.setLoggerName("fhirtest.access");
		retVal.setMessageFormat(
				"Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]");
		retVal.setLogExceptions(true);
		retVal.setErrorMessageFormat("ERROR - ${requestVerb} ${requestUrl}");
		return retVal;
	}

	/**
	 * This interceptor adds some pretty syntax highlighting in responses when a browser is detected
	 */
	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor responseHighlighterInterceptor() {
		ResponseHighlighterInterceptor retVal = new ResponseHighlighterInterceptor();
		return retVal;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor subscriptionSecurityInterceptor() {
		SubscriptionsRequireManualActivationInterceptorDstu3 retVal = new SubscriptionsRequireManualActivationInterceptorDstu3();
		return retVal;
	}

	@Bean()
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

}
