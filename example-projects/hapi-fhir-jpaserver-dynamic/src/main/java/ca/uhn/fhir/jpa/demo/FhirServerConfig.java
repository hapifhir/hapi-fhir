package ca.uhn.fhir.jpa.demo;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.uhn.fhir.jpa.config.BaseJavaConfigDstu3;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptorDstu3;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

/**
 * This is the primary configuration file for the dynamic jpa server running with dstu3 version. 
 * It will load 2 property files: <br>
 *  <i>config/dstu3/immutable.properties</i> and <i>config/dstu3/app_${ENV}.properties</i> <br>
 * where <b>${ENV}</b> is an environment variable named <b>ENV</b> which should be set to one of the strings:
 * 	<i>local, dev, stg or prod</i>.
 *  
 *  By default it will be set to <i>local</i>, so <b>config/dstu3/app_local.properties</b> file will be loaded. 
 *  It expects properties to be exposed either as as environment variables or through property files. Note that environment variable take precedence over
 *  property files.
 *  <ul>
 *  	<li><b>ENV</b> - default value set to <i>"local"</i>. Can be set to <i>"local"</i>, <i>"dev"</i>, <i>"stg"</i> or <i>"prod"</i>.</li>
 *  	<li><b>DB_URL</b> - database url, can be exposed either as environment variable or in environment specific property file, e.g. app_local.properties</li>
 *  	<li><b>DATABASE_URL</b> - this url will be set by Heroku as a db url, if it's set, it'll overwrite db settings set with 
 *  		<b>DB_URL</b></li>
 *  	<li><b>HIBERNATE_CREATE</b> - if set to <b>true</b>, hibernate will drop and recreate schema, if set to <b>false</b>, 
 *  								  will validate the schema
 *  	<li><b>SCHEMA_NAME</b> - if set, schema name will be used in the database url, used only when <b>DATABASE_URL</b> is set.
 *  </ul>
 * 
 */
@Configuration
@EnableTransactionManagement()
@PropertySources({ 
	@PropertySource("classpath:config/dstu3/immutable.properties"),
	@PropertySource("classpath:config/dstu3/app_${ENV:local}.properties") })
public class FhirServerConfig extends BaseJavaConfigDstu3 {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FhirServerConfig.class);
	
	@Autowired
	private Environment env;
	/**
	 * Configure FHIR properties around the the JPA server via this bean
	 */
	@Bean
	public DaoConfig daoConfig() {
		return FhirServerConfigCommon.getDaoConfig();
	}

	/**
	 * The following bean configures the database connection. The 'url' property value of "jdbc:derby:directory:jpaserver_derby_files;create=true" indicates that the server should save resources in a
	 * directory called "jpaserver_derby_files".
	 * 
	 * A URL to a remote database could also be placed here, along with login credentials and other properties supported by BasicDataSource.
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		return FhirServerConfigCommon.getDataSource(env);
	}

	@Override
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		return FhirServerConfigCommon.getEntityManagerFactory(env, dataSource(), fhirContextDstu3());
	}

	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 * @return
	 */
	public LoggingInterceptor loggingInterceptor() {
		return FhirServerConfigCommon.loggingInterceptor();
	}

	/**
	 * This interceptor adds some pretty syntax highlighting in responses when a browser is detected
	 * @return
	 */
	@Bean(autowire = Autowire.BY_TYPE)
	public ResponseHighlighterInterceptor responseHighlighterInterceptor() {
		return FhirServerConfigCommon.getResponseHighlighterInterceptor();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public IServerInterceptor subscriptionSecurityInterceptor() {
		String stuVersion = (env.getProperty(Utils.STU_VERSION) == null)?Utils.DSTU3:env.getProperty(Utils.STU_VERSION) ;
		logger.info("-------STU_VERSION: " + stuVersion);
		SubscriptionsRequireManualActivationInterceptorDstu3 interceptor = new SubscriptionsRequireManualActivationInterceptorDstu3();
		return interceptor;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return FhirServerConfigCommon.getTransactionManager(entityManagerFactory);
	}

}
