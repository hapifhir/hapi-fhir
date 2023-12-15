package ca.uhn.fhir.jpa;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.util.HapiEntityManagerFactoryUtil;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.search.backend.lucene.cfg.LuceneBackendSettings;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@ContextConfiguration(classes = {BaseDaoIT.TestConfig.class})
public class BaseDaoIT {

	@Configuration
	public static class TestConfig {

		@Bean
		public DataSource dataSource(JpaDatabaseContextConfigParamObject theJpaDatabaseContextConfigParamObject){
			return theJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDataSource();
		}

		@Bean
		public JpaEmbeddedDatabase jpaEmbeddedDatabase(JpaDatabaseContextConfigParamObject theJpaDatabaseContextConfigParamObject){
			return theJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase();
		}

		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			ConfigurableListableBeanFactory theConfigurableListableBeanFactory,
			DataSource theDataSource,
			JpaDatabaseContextConfigParamObject theJpaDatabaseContextConfigParamObject) {

			Properties properties = jpaProperties(theJpaDatabaseContextConfigParamObject.getDialect());

			LocalContainerEntityManagerFactoryBean retVal = HapiEntityManagerFactoryUtil.newEntityManagerFactory(theConfigurableListableBeanFactory, FhirContext.forR4(), new JpaStorageSettings());

			retVal.setDataSource(theDataSource);
			retVal.setPersistenceUnitName("PU_HapiFhirJpaR4");
			retVal.setJpaProperties(properties);
			return retVal;
		}

		private Properties jpaProperties(String theDialect) {
			Properties extraProperties = new Properties();
			extraProperties.put("hibernate.format_sql", "false");
			extraProperties.put("hibernate.show_sql", "false");
			extraProperties.put("hibernate.hbm2ddl.auto", "update");
			extraProperties.put("hibernate.dialect", theDialect);
			extraProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "lucene");
			extraProperties.put(
				BackendSettings.backendKey(LuceneBackendSettings.ANALYSIS_CONFIGURER),
				HapiHSearchAnalysisConfigurers.HapiLuceneAnalysisConfigurer.class.getName());
			extraProperties.put(BackendSettings.backendKey(LuceneBackendSettings.LUCENE_VERSION), "LUCENE_CURRENT");

			return extraProperties;
		}

	}

	public static class JpaDatabaseContextConfigParamObject {
		private JpaEmbeddedDatabase myJpaEmbeddedDatabase;
		private String myDialect;

		public static JpaDatabaseContextConfigParamObject of(JpaEmbeddedDatabase theJpaEmbeddedDatabase, String theDialect){
			return new JpaDatabaseContextConfigParamObject(theJpaEmbeddedDatabase, theDialect);
		}

		public JpaDatabaseContextConfigParamObject(JpaEmbeddedDatabase theJpaEmbeddedDatabase, String theDialect) {
			myJpaEmbeddedDatabase = theJpaEmbeddedDatabase;
			myDialect = theDialect;
		}

		public JpaEmbeddedDatabase getJpaEmbeddedDatabase() {
			return myJpaEmbeddedDatabase;
		}

		public String getDialect() {
			return myDialect;
		}
	}

	public static class DaoTestSupport  {
		Logger myLogger;
		@Autowired
		EntityManagerFactory myEntityManagerFactory;
		@Autowired
		JpaEmbeddedDatabase myJpaEmbeddedDatabase;

		public DaoTestSupport(Logger theLogger) {
			myLogger = theLogger;
		}

		public void disableConstraints() {
			myJpaEmbeddedDatabase.disableConstraints();
		}

		public void enableConstraints() {
			myJpaEmbeddedDatabase.enableConstraints();
		}

		public Logger getLogger() {
			return myLogger;
		}


		public EntityManager getEntityManager() {
			return myEntityManagerFactory.createEntityManager();
		}
	}
}


